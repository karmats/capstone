package org.coursera.capstone.entity;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany(mappedBy = "patient")
    private Collection<PatientAnswer> patientAnswers;
    private Date when;

    public Submission() {
    }

    public Submission(List<PatientAnswer> patientAnswers, Date when) {
        this.patientAnswers = patientAnswers;
        this.when = when;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Collection<PatientAnswer> getPatientAnswers() {
        return patientAnswers;
    }

    public void setPatientAnswers(Collection<PatientAnswer> patientAnswers) {
        this.patientAnswers = patientAnswers;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }
}
