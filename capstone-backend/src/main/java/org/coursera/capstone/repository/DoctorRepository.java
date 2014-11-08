package org.coursera.capstone.repository;

import org.coursera.capstone.entity.Doctor;
import org.springframework.data.repository.CrudRepository;

/**
 * An interface for a repository that can store Doctor objects
 */
public interface DoctorRepository extends CrudRepository<Doctor, Long> {

}
