package com.ivoyant.GlobalScheduler.Utils;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestExample {

    public ResponseEntity<String> restCall(String apiUrl, HttpMethod httpMethod) {
        RestTemplate restTemplate = new RestTemplate();

        // Create the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Add any other required headers

        // Create the request entity with headers
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Send the HTTP GET request
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, httpMethod, requestEntity, String.class);

        // Get the response body

        return responseEntity;
        // Process the response data

    }
}
