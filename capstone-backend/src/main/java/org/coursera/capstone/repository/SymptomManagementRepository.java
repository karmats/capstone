package org.coursera.capstone.repository;

import java.util.Collection;

import org.coursera.capstone.client.SymptomManagementApi;
import org.coursera.capstone.entity.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * An interface for a repository that can store Patient objects and allow them
 * to be searched by name.
 * 
 */
@RepositoryRestResource(path = SymptomManagementApi.PATIENT_SVC_PATH)
public interface SymptomManagementRepository extends CrudRepository<Patient, Long> {

    // Find all videos with a matching title (e.g., Video.name)
    Collection<Patient> searchByName(
    // The @Param annotation tells Spring Data Rest which HTTP request
    // parameter it should use to fill in the "name" variable used to
    // search for Patients
            @Param(SymptomManagementApi.NAME_PARAMETER) String name);


}
