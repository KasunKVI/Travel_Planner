package software.kasunkavinda.Travel_Planner.util;

import com.amadeus.Amadeus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import software.kasunkavinda.Travel_Planner.dto.ResponseDto;


@Component
public class FlightUtilities {

    @Value("${amadeus.api.key}")
    private String amadeusAPIKey;

    @Value("${amadeus.api.secret}")
    private String amadeusAPISecret;

    @Value("${amadeus.api.access_token_url}")
    private String TOKEN_URL;


    private final Logger logger = LoggerFactory.getLogger(FlightUtilities.class);

    public Amadeus getAmadeus() {
        Amadeus amadeus = Amadeus.builder(amadeusAPIKey, amadeusAPISecret).build();
        if (amadeus != null) {
            logger.info("Amadeus API connection established successfully");
        } else {
            logger.error("Failed to establish Amadeus API connection");
        }
        return amadeus;
    }

    public String getAccessToken() {

        logger.info("Getting Flight Token");
        // Create a new RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");


        // Set up body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", amadeusAPIKey);
        body.add("client_secret", amadeusAPISecret);

        // Create the HTTP request
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // Make the request
        ResponseEntity<String> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, request, String.class);

        // Parse the response to extract the access_token
        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                // Parse JSON response
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                return root.path("access_token").asText() ;  // Extract the access_token field
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse token response", e);
            }
        } else {
            throw new RuntimeException("Failed to retrieve token: " + response.getStatusCode());
        }
    }
}
