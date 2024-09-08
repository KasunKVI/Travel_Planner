package software.kasunkavinda.Travel_Planner.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
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
import software.kasunkavinda.Travel_Planner.entity.Hotels;
import software.kasunkavinda.Travel_Planner.entity.Itineraries;
import software.kasunkavinda.Travel_Planner.repository.HotelRepo;
import software.kasunkavinda.Travel_Planner.repository.ItinerariesRepo;
import software.kasunkavinda.Travel_Planner.util.LocationUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class HotelService {

    @Value("${tripadvisor.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final LocationUtils locationUtils;

    private final HotelRepo hotelRepo;
    private final ItinerariesRepo itinerariesRepo;

    private final Logger logger = LoggerFactory.getLogger(HotelService.class);

    public ResponseDto<List<HotelDto>> getNearbyHotels(String location) throws IOException {
        logger.info("Getting location id for location: " + location);

        LocationDto locationDto = locationUtils.getLocation(location);

        String url = "https://api.content.tripadvisor.com/api/v1/location/nearby_search?key="+apiKey+"&latLong=" + locationDto.getLat()+"%2C"+locationDto.getLon() + "&category=hotels&language=en";
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("origin", "http://dev.example.local");
        headers.set("referer", "https://dev.example.local");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            // Deserialize the response JSON into HotelResponseDto
            HotelResponseDto hotelResponseDto = objectMapper.readValue(response.getBody(), HotelResponseDto.class);

            return new ResponseDto<>(hotelResponseDto.getData(), "success", "Hotels retrieved successfully");
        } else {
            throw new IOException("Failed to fetch location ID: " + response.getStatusCode());
        }
    }

    public ResponseDto<List<ReviewResponseDto>> getHotelReviews(String locationId) throws IOException {
        logger.info("Getting reviews for location ID: " + locationId);

        String url = "https://api.content.tripadvisor.com/api/v1/location/"+locationId+"/reviews?key="+apiKey+"&language=en";
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("origin", "http://dev.example.local");
        headers.set("referer", "https://dev.example.local");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);



        if (response.getStatusCode().is2xxSuccessful()) {
            // Parse the response JSON
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode data = root.path("data");

            // Map the JSON data to DTOs
            List<ReviewResponseDto> reviews = new ArrayList<>();
            for (JsonNode reviewNode : data) {
                ReviewResponseDto review = mapToReviewDTO(reviewNode);
                reviews.add(review);
            }

            return new ResponseDto<>(reviews, "200", "Reviews retrieved successfully");

        } else {
            throw new RuntimeException("Failed to fetch reviews: " + response.getStatusCode());
        }
    }

    // Helper method to map JSON to ReviewResponseDTO
    private ReviewResponseDto mapToReviewDTO(JsonNode reviewNode) {
        ReviewResponseDto review = new ReviewResponseDto();

        review.setId(reviewNode.path("id").asLong());
        review.setLanguage(reviewNode.path("language").asText());
        review.setLocationId(reviewNode.path("location_id").asLong());
        review.setPublishedDate(reviewNode.path("published_date").asText());
        review.setRating(reviewNode.path("rating").asInt());
        review.setRatingImageUrl(reviewNode.path("rating_image_url").asText());
        review.setUrl(reviewNode.path("url").asText());
        review.setText(reviewNode.path("text").asText());
        review.setTitle(reviewNode.path("title").asText());
        review.setTripType(reviewNode.path("trip_type").asText());
        review.setTravelDate(reviewNode.path("travel_date").asText());

        // Map user details
        UserDto user = new UserDto();
        user.setUsername(reviewNode.path("user").path("username").asText());

        AvatarDto avatar = new AvatarDto();
        JsonNode avatarNode = reviewNode.path("user").path("avatar");
        avatar.setThumbnail(avatarNode.path("thumbnail").asText());
        avatar.setSmall(avatarNode.path("small").asText());
        avatar.setMedium(avatarNode.path("medium").asText());
        avatar.setLarge(avatarNode.path("large").asText());
        avatar.setOriginal(avatarNode.path("original").asText());
        user.setAvatar(avatar);

        review.setUser(user);

        // Map subratings
        JsonNode subratingsNode = reviewNode.path("subratings");
        if (subratingsNode.isArray()) {
            for (JsonNode subrating : subratingsNode) {
                SubratingDto subratingDTO = new SubratingDto();
                subratingDTO.setName(subrating.path("name").asText());
                subratingDTO.setRatingImageUrl(subrating.path("rating_image_url").asText());
                subratingDTO.setValue(subrating.path("value").asInt());
                subratingDTO.setLocalizedName(subrating.path("localized_name").asText());
                review.getSubratings().put(subratingDTO.getName(), subratingDTO);
            }
        }

        // Map owner response
        JsonNode ownerResponseNode = reviewNode.path("owner_response");
        if (ownerResponseNode != null && !ownerResponseNode.isMissingNode()) {
            OwnerResponseDto ownerResponse = new OwnerResponseDto();
            ownerResponse.setId(ownerResponseNode.path("id").asLong());
            ownerResponse.setTitle(ownerResponseNode.path("title").asText());
            ownerResponse.setText(ownerResponseNode.path("text").asText());
            ownerResponse.setLanguage(ownerResponseNode.path("language").asText());
            ownerResponse.setAuthor(ownerResponseNode.path("author").asText());
            ownerResponse.setPublishedDate(ownerResponseNode.path("published_date").asText());
            review.setOwnerResponse(ownerResponse);
        }

        return review;
    }

    public ResponseDto<List<HotelPhotosDto>> getHotelPhotos(String locationId) throws IOException {
        logger.info("Getting photos for location ID: " + locationId);

        String url = "https://api.content.tripadvisor.com/api/v1/location/"+locationId+"/photos?key="+apiKey+"&language=en";
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("origin", "http://dev.example.local");
        headers.set("referer", "https://dev.example.local");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
                // Parse the response body
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(response.getBody());
                JsonNode dataNode = rootNode.path("data");

                List<HotelPhotosDto> imageDTOs = new ArrayList<>();

                // Extract the image data
                for (JsonNode imageNode : dataNode) {
                    long id = imageNode.path("id").asLong();
                    String caption = imageNode.path("caption").asText();
                    String publishedDate = imageNode.path("published_date").asText();

                    // Assuming we want the "large" image size
                    String size = "large";
                    String urlImg = imageNode.path("images").path(size).path("url").asText();

                    // Create DTO and add to list
                    HotelPhotosDto imageDTO = new HotelPhotosDto(id, caption, publishedDate, size, urlImg);
                    imageDTOs.add(imageDTO);
                }

                return new ResponseDto<>(imageDTOs, "200", "Photos retrieved successfully");


        } else {
            throw new RuntimeException("Failed to fetch photos: " + response.getStatusCode());
        }
    }

    public ResponseDto<HotelDto> saveHotel(HotelDto hotelDto) {
        logger.info("Saving hotel: " + hotelDto.getName());

        Itineraries itineraries = itinerariesRepo.getReferenceById(hotelDto.getItineraryId());
        // Save the hotel to the database
        Hotels hotel = new Hotels();
        hotel.setName(hotelDto.getName());
        hotel.setAddress(hotelDto.getAddress_obj().getAddress_string());
        hotel.setLocation_id(hotelDto.getLocation_id());
        hotel.setBearing(hotelDto.getBearing());
        hotel.setDistance(hotelDto.getDistance());
        hotel.setItinerary(itineraries);
        if (hotel.getLocation_id() != null) {
            hotelRepo.save(hotel);
            return new ResponseDto<>(hotelDto, "200", "Hotel saved successfully");
        } else {
            return new ResponseDto<>(hotelDto, "400", "Failed to save hotel");
        }
    }

    public ResponseDto<HotelDto> getHotel(String locationId) {
        logger.info("Getting hotel with location ID: " + locationId);
        Hotels hotel = hotelRepo.findById(locationId).orElse(null);
        AddressDto addressDto = new AddressDto();
        if (hotel != null) {
            addressDto.setAddress_string(hotel.getAddress());
            HotelDto hotelDto = new HotelDto();
            hotelDto.setName(hotel.getName());
            hotelDto.setAddress_obj(addressDto);
            hotelDto.setLocation_id(hotel.getLocation_id());
            hotelDto.setBearing(hotel.getBearing());
            hotelDto.setDistance(hotel.getDistance());
            return new ResponseDto<>(hotelDto, "200", "Hotel retrieved successfully");
        } else {
            return new ResponseDto<>(null, "400", "Hotel not found");
        }
    }

    public ResponseDto<String> deleteHotel(String locationId) {
        logger.info("Deleting hotel with location ID: " + locationId);
        hotelRepo.deleteById(locationId);

            return new ResponseDto<>(locationId, """
                    200""", "Hotel deleted successfully");

    }
}
