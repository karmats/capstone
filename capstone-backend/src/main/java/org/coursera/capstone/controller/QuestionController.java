package org.coursera.capstone.controller;

import org.coursera.capstone.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepo;
    
    
}
