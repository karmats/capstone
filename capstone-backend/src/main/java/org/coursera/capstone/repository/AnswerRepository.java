package org.coursera.capstone.repository;

import org.coursera.capstone.entity.Answer;
import org.springframework.data.repository.CrudRepository;

/**
 * An interface for a repository that can store Answer objects
 * 
 * @author matros
 *
 */
public interface AnswerRepository extends CrudRepository<Answer, Long> {

}
