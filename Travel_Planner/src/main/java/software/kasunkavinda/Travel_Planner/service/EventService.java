package software.kasunkavinda.Travel_Planner.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import software.kasunkavinda.Travel_Planner.dto.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import software.kasunkavinda.Travel_Planner.util.LocationUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    @Value("${ticketmaster.api.key}")
    private String apiKey;

    @Value("${ticketmaster.api.base_url}")
    private String baseUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final LocationUtils locationUtils;

    public ResponseDto<EventResponseDto> getNearbyEvents(String location) throws JsonProcessingException {

        // Get location details
        LocationDto lCode = locationUtils.getLocation(location);
        double lat = lCode.getLat();
        double lon = lCode.getLon();
        int radius = 50;
        String url = baseUrl + "/events.json?apikey=" + apiKey +"&latlong=" + lat + "," + lon + "&radius=" + radius+"&size=3";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Parse response
        EventResponseDto eventResponse = new EventResponseDto();

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode eventsNode = root.path("_embedded").path("events");

            List<EventDto> events = new ArrayList<>();
            for (JsonNode eventNode : eventsNode) {
                EventDto eventDto = new EventDto();
                eventDto.setName(eventNode.path("name").asText());
                eventDto.setType(eventNode.path("type").asText());
                eventDto.setUrl(eventNode.path("url").asText());
                eventDto.setDateTime(eventNode.path("dates").path("start").path("dateTime").asText());
                eventDto.setStatus(eventNode.path("dates").path("status").path("code").asText());

                JsonNode venueNode = eventNode.path("_embedded").path("venues").get(0);
                VenueDto venueDto = new VenueDto();
                venueDto.setName(venueNode.path("name").asText());
                venueDto.setUrl(venueNode.path("url").asText());
                venueDto.setCity(venueNode.path("city").path("name").asText());
                venueDto.setCountry(venueNode.path("country").path("name").asText());
                venueDto.setAddress(venueNode.path("address").path("line1").asText());
                venueDto.setLatitude(venueNode.path("location").path("latitude").asDouble());
                venueDto.setLongitude(venueNode.path("location").path("longitude").asDouble());
                eventDto.setVenue(venueDto);

                List<ImageDto> images = new ArrayList<>();
                JsonNode imagesNode = eventNode.path("images");
                for (JsonNode imageNode : imagesNode) {
                    ImageDto imageDto = new ImageDto();
                    imageDto.setRatio(imageNode.path("ratio").asText());
                    imageDto.setUrl(imageNode.path("url").asText());
                    images.add(imageDto);
                }
                eventDto.setImages(images);

                events.add(eventDto);
            }
            eventResponse.setEvents(events);


        return new ResponseDto<>(eventResponse, "Success", "Events for " + location);
    }
}
