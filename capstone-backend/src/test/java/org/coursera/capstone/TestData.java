package org.coursera.capstone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.coursera.capstone.client.SymptomManagementApi;
import org.coursera.capstone.dto.CheckInRequestDto;
import org.coursera.capstone.entity.Answer;
import org.coursera.capstone.entity.PainMedication;
import org.coursera.capstone.entity.Question;


public class TestData {


    public static CheckInRequestDto createCheckInRequest(SymptomManagementApi api, Long patientMedicalRecordNo, Date checkInTime) {
        // Medications
        List<PainMedication> painMedicationsInDb = InitialTestData.createPainMedications();
        List<CheckInRequestDto.MedicationTakenDto> medications = new ArrayList<CheckInRequestDto.MedicationTakenDto>();
        medications.add(new CheckInRequestDto.MedicationTakenDto(painMedicationsInDb.get(0).getMedicationId(), true,
                new Date()));
        // Questions
        List<Question> questionsFromWs = api.getQuestions();
        Question answeredQuestion = questionsFromWs.get(0);
        Answer questionAnswer = new ArrayList<>(answeredQuestion.getAnswers()).get(0);
        List<CheckInRequestDto.PatientAnswerDto> patientAnswers = new ArrayList<>();
        patientAnswers.add(new CheckInRequestDto.PatientAnswerDto(answeredQuestion.getId(), questionAnswer.getId()));
        return new CheckInRequestDto(patientMedicalRecordNo, patientAnswers, medications, checkInTime.getTime());
    }
}
