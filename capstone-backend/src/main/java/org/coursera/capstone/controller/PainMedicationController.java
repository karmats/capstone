package org.coursera.capstone.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.coursera.capstone.client.SymptomManagementApi;
import org.coursera.capstone.dto.PainMedicationDto;
import org.coursera.capstone.entity.PainMedication;
import org.coursera.capstone.entity.Patient;
import org.coursera.capstone.repository.PainMedicationRepository;
import org.coursera.capstone.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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
    @Autowired
    PatientRepository patientRepo;

    /**
     * Get all available pain medications
     * 
     * @return A list with all pain medications
     */
    @RequestMapping(value = SymptomManagementApi.PAIN_MEDICATION_SVC_PATH, method = RequestMethod.GET)
    public @ResponseBody Collection<PainMedicationDto> getAllPainMedications() {
        List<PainMedicationDto> result = new ArrayList<PainMedicationDto>();
        Iterable<PainMedication> painMedications = painMedicationRepo.findAll();
        for (PainMedication pm : painMedications) {
            result.add(new PainMedicationDto(pm));
        }
        return result;
    }

    /**
     * Updates a medication plan for a specific patient
     * 
     * @param username
     *            The username of the patient to update the medications for
     * @param painMedications
     *            The new pain medication plan
     */
    @RequestMapping(value = SymptomManagementApi.PAIN_MEDICATION_UPDATE_SVC_PATH, method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void updatePainMedication(@PathVariable(SymptomManagementApi.USERNAME_PARAMETER) String username,
            Collection<PainMedicationDto> painMedications) {
        Patient p = patientRepo.findByUsername(username);
        List<PainMedication> updatedMedications = new ArrayList<>();
        for (PainMedicationDto pmd : painMedications) {
            PainMedication pm = painMedicationRepo.findByMedicationId(pmd.getMedicationId());
            updatedMedications.add(pm);
        }
        p.setPainMedications(updatedMedications);
        patientRepo.save(p);
    }
}
