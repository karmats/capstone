package org.coursera.capstone.controller;

import org.coursera.capstone.client.SymptomManagementApi;
import org.coursera.capstone.dto.CheckInDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CheckInController {

    @RequestMapping(value = SymptomManagementApi.CHECK_IN_SVC_PATH, method = RequestMethod.POST)
    public @ResponseBody CheckInDto checkIn(@RequestBody CheckInDto checkIn) {
        System.out.println("Got checkin answers " + checkIn.getPatientAnswers().size() + " medications "
                + checkIn.getMedicationsTaken().size() + " when: " + checkIn.getWhen());
        return checkIn;
    }
}
