package org.coursera.capstone.repository;

import org.coursera.capstone.entity.PainMedication;
import org.springframework.data.repository.CrudRepository;

/**
 * An interface for a repository that can store Pain medication objects
 * 
 * @author matros
 *
 */
public interface PainMedicationRepository extends CrudRepository<PainMedication, Long> {

    PainMedication findByMedicationId(String medicationId);
}
