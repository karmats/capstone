package org.coursera.capstone.repository;

import org.coursera.capstone.client.SymptomManagementApi;
import org.coursera.capstone.entity.Doctor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * An interface for a repository that can store Doctor objects
 */
@RepositoryRestResource(path = SymptomManagementApi.DOCTOR_SVC_PATH)
public interface DoctorRepository extends CrudRepository<Doctor, Long> {

}
