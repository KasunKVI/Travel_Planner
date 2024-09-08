package software.kasunkavinda.Travel_Planner.util;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import software.kasunkavinda.Travel_Planner.dto.LocationDto;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LocationUtils {

    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${google.api.key}")
    private String googleApiKey;

    private final String baseUrl = "http://api.openweathermap.org/";

    private final RestTemplate restTemplate;

    Logger logger = LoggerFactory.getLogger(LocationUtils.class);

    public LocationDto getLocation(String location) {

        logger.info("Getting location for {}", location);
        String url = baseUrl + "geo/1.0/direct?q=" + location + "&limit=1&appid=" + apiKey;

        logger.info("URL: {}", url);

        // Set headers (if needed in the future, currently not used)
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make the API call
        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);

        // Check response status
        if (response.getStatusCode().is2xxSuccessful()) {
            logger.info("Location details retrieved successfully.");

            // Parse the response
            List<Map<String, Object>> responseBody = response.getBody();
            if (responseBody != null && !responseBody.isEmpty()) {
                Map<String, Object> locationData = responseBody.get(0);

                // Extract latitude and longitude
                double lat = (Double) locationData.get("lat");
                double lon = (Double) locationData.get("lon");

                logger.info("Location details: Latitude: {}, Longitude: {}", lat, lon);
                // Create and return LocationDto
                return new LocationDto(lat, lon);

            } else {
                logger.error("No location data found for {}", location);
                throw new RuntimeException("No location data found.");
            }
        } else {
            logger.error("Failed to retrieve location details. Status code: {}", response.getStatusCode());
            throw new RuntimeException("Failed to retrieve location details.");
        }
    }
    public String getLocationId(String textQuery) {
        String url = "https://places.googleapis.com/v1/places:searchText";

        // Create request headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("X-Goog-Api-Key", googleApiKey);
        headers.set("X-Goog-FieldMask", "places.id,places.displayName,places.formattedAddress");

        // Create request body
        JsonObject body = new JsonObject();
        body.addProperty("textQuery", textQuery);

        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

        // Send POST request
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to search places: " + response.getStatusCode());
        }
    }

}
