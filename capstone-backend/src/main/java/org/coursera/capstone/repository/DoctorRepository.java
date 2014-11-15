package org.coursera.capstone.repository;

import org.coursera.capstone.client.SymptomManagementApi;
import org.coursera.capstone.entity.Doctor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * An interface for a repository that can store Doctor objects
 */
public interface DoctorRepository extends CrudRepository<Doctor, Long> {

    // Find doctor by its username
    Doctor findByUsername(@Param(SymptomManagementApi.USERNAME_PARAMETER) String username);
}
