package org.coursera.capstone.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.coursera.capstone.client.SymptomManagementApi;
import org.coursera.capstone.dto.PainMedicationDto;
import org.coursera.capstone.entity.PainMedication;
import org.coursera.capstone.repository.PainMedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller to handle pain medications
 * 
 * @author matros
 *
 */
@Controller
public class PainMedicationController {

    @Autowired
    PainMedicationRepository painMedicationRepo;

    @RequestMapping(value = SymptomManagementApi.PAIN_MEDICATION_SVC_PATH, method = RequestMethod.GET)
    public @ResponseBody Collection<PainMedicationDto> getAllPainMedications() {
        List<PainMedicationDto> result = new ArrayList<PainMedicationDto>();
        Iterable<PainMedication> painMedications = painMedicationRepo.findAll();
        for (PainMedication pm : painMedications) {
            result.add(new PainMedicationDto(pm));
        }
        return result;
    }
}
