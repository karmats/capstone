package org.coursera.capstone.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * A simple object to represent a Patients answer
 */
@Entity
public class PatientAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private Question question;
    @ManyToOne
    private Answer answer;
    @ManyToOne
    private CheckIn answerCheckIn;

    public PatientAnswer() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public CheckIn getAnswerCheckIn() {
        return answerCheckIn;
    }

    public void setAnswerCheckIn(CheckIn answerCheckIn) {
        this.answerCheckIn = answerCheckIn;
    }

}
