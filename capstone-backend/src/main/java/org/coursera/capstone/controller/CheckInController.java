package org.coursera.capstone.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.coursera.capstone.client.SymptomManagementApi;
import org.coursera.capstone.dto.CheckInDto;
import org.coursera.capstone.dto.CheckInDto.MedicationTakenDto;
import org.coursera.capstone.dto.CheckInDto.PatientAnswerDto;
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
     *            The {@link CheckInDto} to persist
     */
    @RequestMapping(value = SymptomManagementApi.CHECK_IN_SVC_PATH, method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void checkIn(@RequestBody CheckInDto checkIn) {
        CheckIn checkInEntity = new CheckIn();
        checkInEntity.setWhen(checkIn.getWhen());
        // The patient made the check-in
        Patient p = patientRepo.findByMedicalRecordNumber(checkIn.getPatientMedicalRecordNumber());
        checkInEntity.setPatient(p);
        // The answers
        List<PatientAnswer> patientAnswers = new ArrayList<PatientAnswer>();
        for (PatientAnswerDto paDto : checkIn.getPatientAnswers()) {
            if (paDto.getAnswerId() == null) {
                continue;
            }
            PatientAnswer pa = new PatientAnswer(questionRepo.findOne(paDto.getQuestionId()), answerRepo.findOne(paDto
                    .getAnswerId()));
            patientAnswerRepo.save(pa);
            patientAnswers.add(pa);
        }
        checkInEntity.setPatientAnswers(patientAnswers);
        // The pain medications taken
        List<PatientMedicationTaken> medicationsTaken = new ArrayList<PatientMedicationTaken>();
        for (MedicationTakenDto mtDto : checkIn.getMedicationsTaken()) {
            if (mtDto.isTaken()) {
                PatientMedicationTaken pmt = new PatientMedicationTaken(mtDto.getWhen(),
                        painMedicationRepo.findByMedicationId(mtDto.getMedicationId()));
                patientMedicationTakenRepo.save(pmt);
                medicationsTaken.add(pmt);
            }
        }
        checkInEntity.setPatientMedicationsTaken(medicationsTaken);
        // Save to db
        checkInRepo.save(checkInEntity);
    }

    @RequestMapping(value = SymptomManagementApi.CHECK_IN_SVC_PATH, method = RequestMethod.GET)
    public @ResponseBody Collection<CheckIn> getAllCheckIns() {
        return Lists.newArrayList(checkInRepo.findAll());
    }

    @RequestMapping(value = SymptomManagementApi.CHECK_IN_PATIENT_SVC_PATH, method = RequestMethod.GET)
    public @ResponseBody Collection<CheckIn> getCheckInsForPatient(
            @PathVariable(SymptomManagementApi.USERNAME_PARAMETER) String username) {
        Collection<CheckIn> checkIns = checkInRepo.findByPatientUsername(username);
        return checkIns;
    }
}
