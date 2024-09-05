package software.kasunkavinda.Travel_Planner.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import software.kasunkavinda.Travel_Planner.dto.*;
import software.kasunkavinda.Travel_Planner.util.FlightUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightUtilities flightUtilities;
    private final RestTemplate restTemplate;

    @Value("${amadeus.api.base_url}")
    private String baseUrl;

    private String access_token;
    private final Logger logger= LoggerFactory.getLogger(FlightService.class);

    public String getAccessToken(){
        return String.valueOf(flightUtilities.getAccessToken());
    }
    public List<FlightOfferDto> getFlightDetails(FlightDetailsDto flightDetailsDto) {
        logger.info("Request received to get flight offers.");
        String url = baseUrl + "/shopping/flight-offers" +
                "?originLocationCode=" + flightDetailsDto.getOrigin() +
                "&destinationLocationCode=" + flightDetailsDto.getDestination() +
                "&departureDate=" + flightDetailsDto.getDepartureDate() +
                "&adults=" + flightDetailsDto.getAdults() +
                "&max=" + flightDetailsDto.getMax();

        logger.info("URL: " + url);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getAccessToken());

        logger.info("Access Token: " + getAccessToken());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            logger.info("Flight details retrieved successfully.");
            return parseFlightOffers(response.getBody());
        } else {
            logger.error("Failed to retrieve flight details. Status code: " + response.getStatusCode());
            throw new RuntimeException("Failed to retrieve flight details.");
        }
    }
    private List<FlightOfferDto> parseFlightOffers(Map<String, Object> responseBody) {
        List<FlightOfferDto> flightOffers = new ArrayList<>();
        List<Map<String, Object>> offers = (List<Map<String, Object>>) responseBody.get("data");

        for (Map<String, Object> offer : offers) {
            FlightOfferDto flightOffer = new FlightOfferDto();
            flightOffer.setId((String) offer.get("id"));
            flightOffer.setLastTicketingDate((String) offer.get("lastTicketingDate"));
            flightOffer.setNumberOfBookableSeats((Integer) offer.get("numberOfBookableSeats"));

            Map<String, Object> price = (Map<String, Object>) offer.get("price");
            flightOffer.setPrice(new PriceDto(
                    (String) price.get("currency"),
                    (String) price.get("total"),
                    (String) price.get("base")
            ));

            List<ItineraryDto> itineraries = new ArrayList<>();
            List<Map<String, Object>> itineraryMaps = (List<Map<String, Object>>) offer.get("itineraries");

            for (Map<String, Object> itineraryMap : itineraryMaps) {
                ItineraryDto itinerary = new ItineraryDto();
                itinerary.setDuration((String) itineraryMap.get("duration"));

                List<SegmentDto> segments = new ArrayList<>();
                List<Map<String, Object>> segmentMaps = (List<Map<String, Object>>) itineraryMap.get("segments");

                for (Map<String, Object> segmentMap : segmentMaps) {
                    SegmentDto segment = new SegmentDto();
                    Map<String, Object> departure = (Map<String, Object>) segmentMap.get("departure");
                    Map<String, Object> arrival = (Map<String, Object>) segmentMap.get("arrival");
                    Map<String, Object> aircraft = (Map<String, Object>) segmentMap.get("aircraft");

                    segment.setDepartureAirport((String) departure.get("iataCode"));
                    segment.setDepartureTime((String) departure.get("at"));
                    segment.setArrivalAirport((String) arrival.get("iataCode"));
                    segment.setArrivalTime((String) arrival.get("at"));
                    segment.setCarrierCode((String) segmentMap.get("carrierCode"));
                    segment.setAircraftCode((String) aircraft.get("code"));
                    segment.setNumberOfStops((Integer) segmentMap.get("numberOfStops"));

                    segments.add(segment);
                }

                itinerary.setSegments(segments);
                itineraries.add(itinerary);
            }

            flightOffer.setItineraries(itineraries);
            flightOffers.add(flightOffer);
        }

        return flightOffers;
    }


}
