package org.coursera.capstone.repository;

import org.coursera.capstone.entity.Question;
import org.springframework.data.repository.CrudRepository;

/**
 * An interface for a repository that can store Question objects.
 * 
 * @author matros
 *
 */
public interface QuestionRepository extends CrudRepository<Question, Long> {

}
