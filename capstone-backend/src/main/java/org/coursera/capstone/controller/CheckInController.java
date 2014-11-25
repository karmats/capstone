package org.coursera.capstone.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.coursera.capstone.client.SymptomManagementApi;
import org.coursera.capstone.dto.CheckInPatientResponseDto;
import org.coursera.capstone.dto.CheckInRequestDto;
import org.coursera.capstone.dto.CheckInRequestDto.MedicationTakenDto;
import org.coursera.capstone.dto.CheckInRequestDto.PatientAnswerDto;
import org.coursera.capstone.entity.CheckIn;
import org.coursera.capstone.entity.Patient;
import org.coursera.capstone.entity.PatientAnswer;
import org.coursera.capstone.entity.PatientMedicationTaken;
import org.coursera.capstone.repository.AnswerRepository;
import org.coursera.capstone.repository.CheckInRepository;
import org.coursera.capstone.repository.PainMedicationRepository;
import org.coursera.capstone.repository.PatientAnswerRepository;
import org.coursera.capstone.repository.PatientMedicationTakenRepository;
import org.coursera.capstone.repository.PatientRepository;
import org.coursera.capstone.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.common.collect.Lists;

@Controller
public class CheckInController {

    @Autowired
    CheckInRepository checkInRepo;
    @Autowired
    PatientRepository patientRepo;
    @Autowired
    QuestionRepository questionRepo;
    @Autowired
    AnswerRepository answerRepo;
    @Autowired
    PainMedicationRepository painMedicationRepo;
    @Autowired
    PatientMedicationTakenRepository patientMedicationTakenRepo;
    @Autowired
    PatientAnswerRepository patientAnswerRepo;

    /**
     * Saves a patient check-in
     * 
     * @param checkIn
     *            The {@link CheckInRequestDto} to persist
     */
    @RequestMapping(value = SymptomManagementApi.CHECK_IN_SVC_PATH, method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void checkIn(@RequestBody CheckInRequestDto checkIn) {
        CheckIn checkInEntity = new CheckIn();
        checkInEntity.setCheckInTime(checkIn.getWhen());
        checkInRepo.save(checkInEntity);
        // The patient made the check-in
        Patient p = patientRepo.findByMedicalRecordNumber(checkIn.getPatientMedicalRecordNumber());
        checkInEntity.setPatient(p);
        // The answers
        List<PatientAnswer> patientAnswers = new ArrayList<PatientAnswer>();
        for (PatientAnswerDto paDto : checkIn.getPatientAnswers()) {
            // TODO This should be removed when fixing stuffs in client
            if (paDto.getAnswerId() == null) {
                continue;
            }
            PatientAnswer pa = new PatientAnswer(questionRepo.findOne(paDto.getQuestionId()), answerRepo.findOne(paDto
                    .getAnswerId()));
            pa.setAnswerCheckIn(checkInEntity);
            patientAnswerRepo.save(pa);
            patientAnswers.add(pa);
        }
        checkInEntity.setPatientAnswers(patientAnswers);
        // Check if an alert is necessary.
        checkInEntity.setAlert(checkAlert(patientAnswers, checkIn.getPatientMedicalRecordNumber()));
        // The pain medications taken
        List<PatientMedicationTaken> medicationsTaken = new ArrayList<PatientMedicationTaken>();
        for (MedicationTakenDto mtDto : checkIn.getMedicationsTaken()) {
            if (mtDto.isTaken()) {
                PatientMedicationTaken pmt = new PatientMedicationTaken(mtDto.getWhen(),
                        painMedicationRepo.findByMedicationId(mtDto.getMedicationId()));
                pmt.setMedicationCheckIn(checkInEntity);
                patientMedicationTakenRepo.save(pmt);
                medicationsTaken.add(pmt);
            }
        }
        checkInEntity.setPatientMedicationsTaken(medicationsTaken);
        // Save to db
        checkInRepo.save(checkInEntity);
    }

    /**
     * Checks if an alert is necessary by getting past check in answers and see if they are "alert" answers
     * 
     * @param patientAnswers
     *            The new patient check-in answer
     * @param patientMedicalRecordNo
     *            The patient medical record number
     * @return The alert text or null if this doesn't trigger an alert
     */
    private String checkAlert(List<PatientAnswer> patientAnswers, Long patientMedicalRecordNo) {
        Date now = new Date();
        Calendar from = Calendar.getInstance();
        from.add(Calendar.DATE, -1);
        Collection<CheckIn> last24HoursCheckIns = checkInRepo.findByCheckInTime(patientMedicalRecordNo, from.getTime(),
                now);
        String alert = "";
        for (PatientAnswer pa : patientAnswers) {
            int hoursBeforeAlert = pa.getAnswer().getHoursBeforeAlert();
            if (hoursBeforeAlert > 0) {
                int timePast = 0;
                // Check previous answers to see what the patient has answered
                // The check-ins comes as sorted descending, i.e. newest first
                for (CheckIn pastCi : last24HoursCheckIns) {
                    for (PatientAnswer pastPa : pastCi.getPatientAnswers()) {
                        if (pastPa.getQuestion().equals(pa.getQuestion())) {
                            // Patient answered bad this question as well, add the time that has
                            // passed since this check-in
                            if (pastPa.getAnswer().getHoursBeforeAlert() > 0) {
                                timePast += (int) (now.getTime() - pastCi.getCheckInTime().getTime())
                                        / (1000 * 60 * 60);
                                if (timePast >= pa.getAnswer().getHoursBeforeAlert()) {
                                    // The time has passed return the answer alert text
                                    alert += alert.isEmpty() ? pa.getAnswer().getAlertText() : CheckIn.AlERTS_DELIMITER
                                            + pa.getAnswer().getAlertText();
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }
        return alert;
    }

    @RequestMapping(value = SymptomManagementApi.CHECK_IN_SVC_PATH, method = RequestMethod.GET)
    public @ResponseBody Collection<CheckIn> getAllCheckIns() {
        return Lists.newArrayList(checkInRepo.findAll());
    }

    @RequestMapping(value = SymptomManagementApi.CHECK_IN_PATIENT_SVC_PATH, method = RequestMethod.GET)
    public @ResponseBody Collection<CheckInPatientResponseDto> getCheckInsForPatient(
            @PathVariable(SymptomManagementApi.USERNAME_PARAMETER) String username) {
        List<CheckInPatientResponseDto> result = new ArrayList<>();
        Collection<CheckIn> checkIns = checkInRepo.findByPatientUsername(username);
        for (CheckIn checkIn : checkIns) {
            result.add(new CheckInPatientResponseDto(checkIn));
        }
        return result;
    }
}
