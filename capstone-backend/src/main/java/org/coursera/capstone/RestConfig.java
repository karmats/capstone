package org.coursera.capstone;

import org.coursera.capstone.entity.Answer;
import org.coursera.capstone.entity.Question;
import org.coursera.capstone.json.ResourcesMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RestConfig extends RepositoryRestMvcConfiguration {

    // We are overriding the bean that RepositoryRestMvcConfiguration
    // is using to convert our objects into JSON so that we can control
    // the format. The Spring dependency injection will inject our instance
    // of ObjectMapper in all of the spring data rest classes that rely
    // on the ObjectMapper. This is an example of how Spring dependency
    // injection allows us to easily configure dependencies in code that
    // we don't have easy control over otherwise.
    @Override
    public ObjectMapper halObjectMapper() {
        return new ResourcesMapper();
    }

    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        // We need to expose the ids for the answer and question class to know what question and what answer the patient
        // answers
        config.exposeIdsFor(Answer.class, Question.class);
        super.configureRepositoryRestConfiguration(config);
    }
}
