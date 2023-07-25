package com.FinalTestSuite.main.testEx;


import com.FinalTestSuite.main.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllUsers() throws Exception {
        // Prepare HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        // Perform HTTP GET request
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/users",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        // Parse the JSON response into a list of User objects
        List<User> users = Arrays.asList(objectMapper.readValue(response.getBody(), User[].class));

        // Validate the response using assertions
        assertEquals(200, response.getStatusCodeValue());


        User user1 = users.get(0);
        assertEquals("Bharath1 ", user1.getUsername());
        assertEquals("Bharath1@gmail.com", user1.getEmail());

        User user2 = users.get(1);
        assertEquals("Bharath2 ", user2.getUsername());
        assertEquals("Bharath2@gmail.com", user2.getEmail());
    }


}
