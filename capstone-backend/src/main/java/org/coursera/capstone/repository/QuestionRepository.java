package org.coursera.capstone.repository;

import org.coursera.capstone.client.SymptomManagementApi;
import org.coursera.capstone.entity.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * An interface for a repository that can store Question objects.
 * 
 * @author matros
 *
 */
@RepositoryRestResource(path = SymptomManagementApi.QUESTION_SVC_PATH)
public interface QuestionRepository extends CrudRepository<Question, Long> {

}
