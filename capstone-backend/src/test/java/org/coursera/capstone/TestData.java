package org.coursera.capstone;

import java.util.UUID;

import org.coursera.capstone.repository.Patient;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is a utility class to aid in the construction of Video objects with
 * random names, urls, and durations. The class also provides a facility to
 * convert objects into JSON using Jackson, which is the format that the
 * VideoSvc controller is going to expect data in for integration testing.
 * 
 * @author jules
 *
 */
public class TestData {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Construct and return a Patient object with a random name
     * 
     * @return
     */
    public static Patient randomVideo() {
        // Information about the video
        // Construct a random identifier using Java's UUID class
        String id = UUID.randomUUID().toString();
        String name = "Name-" + id;
        return new Patient(name);
    }

    /**
     * Convert an object to JSON using Jackson's ObjectMapper
     * 
     * @param o
     * @return
     * @throws Exception
     */
    public static String toJson(Object o) throws Exception {
        return objectMapper.writeValueAsString(o);
    }
}
