package org.coursera.capstone.repository;

import java.util.Collection;
import java.util.Date;

import org.coursera.capstone.entity.CheckIn;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Repository for check-in data
 */
public interface CheckInRepository extends CrudRepository<CheckIn, Long> {

    @Query("select ci from CheckIn ci where ci.patient.username = :patientUsername order by checkInTime desc")
    public Collection<CheckIn> findByPatientUsername(@Param("patientUsername") String patientUsername);

    @Query("select ci from CheckIn ci where ci.patient.medicalRecordNumber = :medicalRecordNumber and ci.checkInTime between :from and :to order by checkInTime desc")
    public Collection<CheckIn> findByCheckInTime(@Param("medicalRecordNumber") Long patientUserName,
            @Param("from") Date from, @Param("to") Date to);
}
