package org.coursera.capstone.repository;

import java.util.Collection;

import org.coursera.capstone.entity.CheckIn;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Repository for check-in data
 */
public interface CheckInRepository extends CrudRepository<CheckIn, Long> {

    @Query("select ci from CheckIn ci where ci.patient.username = :patientUsername")
    public Collection<CheckIn> findByPatientUsername(@Param("patientUsername") String patientUsername);
}
