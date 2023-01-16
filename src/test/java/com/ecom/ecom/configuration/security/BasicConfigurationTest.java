package com.ecom.ecom.configuration.security;

import com.ecom.ecom.configuration.EcomSecurity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import({BasicConfiguration.class, EcomSecurity.class})
@SpringBootTest
public class BasicConfigurationTest {
    TestRestTemplate restTemplate;
    URL base;

    @BeforeAll
    void setUp() throws MalformedURLException {
        restTemplate = new TestRestTemplate("user", "user");
        base = new URL("http://localhost:8080");
    }

//    @Test
//    public void whenLoggedUserRequestsHomePage_ThenSuccess()
//            throws IllegalStateException {
//        final ResponseEntity<String> response =
//                restTemplate.getForEntity(base.toString(), String.class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertTrue(Objects.requireNonNull(response.getBody()).contains("Baeldung"));
//    }

    @Test
    public void whenUserWithWrongCredentials_thenUnauthorizedPage()
            throws Exception {

        restTemplate = new TestRestTemplate("user", "wrongpassword");
        final ResponseEntity<String> response =
                restTemplate.getForEntity(base.toString(), String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("Unauthorized"));
    }
}
