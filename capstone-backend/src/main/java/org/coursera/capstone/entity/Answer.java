package org.coursera.capstone.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A simple object to represent a Answer
 */
@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String text;
    // The number of hours before an alert will be triggered
    private Integer hoursBeforeAlert;
    // The text when an alert is triggered
    private String alertText;

    @JsonIgnore
    @ManyToOne
    private Question question;

    public Answer() {
    }

    public Answer(String text, Integer hoursBeforeAlert, String alertText, Question question) {
        this.text = text;
        this.hoursBeforeAlert = hoursBeforeAlert;
        this.alertText = alertText; 
        this.question = question;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getHoursBeforeAlert() {
        return hoursBeforeAlert;
    }

    public void setHoursBeforeAlert(Integer hoursForAlert) {
        this.hoursBeforeAlert = hoursForAlert;
    }

    public String getAlertText() {
        return alertText;
    }

    public void setAlertText(String alertText) {
        this.alertText = alertText;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

}
