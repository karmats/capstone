package org.coursera.capstone.repository;

import org.coursera.capstone.entity.Answer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * An interface for a repository that can store Answer objects
 * 
 * @author matros
 *
 */
@RepositoryRestResource(exported = false)
public interface AnswerRepository extends CrudRepository<Answer, Long> {

}
