package org.coursera.capstone.repository;

import org.coursera.capstone.entity.CheckIn;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for check-in data
 */
public interface CheckInRepository extends CrudRepository<CheckIn, Long> {

}
