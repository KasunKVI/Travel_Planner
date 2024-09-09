package software.kasunkavinda.Travel_Planner.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.kasunkavinda.Travel_Planner.dto.LocationDto;
import software.kasunkavinda.Travel_Planner.dto.PlaceResponseDto;
import software.kasunkavinda.Travel_Planner.dto.ResponseDto;
import software.kasunkavinda.Travel_Planner.util.LocationUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    @Value("${foursqure.api.key}")
    private String foursqureApiKey;

    private final LocationUtils locationUtils;

    public ResponseDto<List<PlaceResponseDto>> getRecommendations(String type, String location) throws Exception {

        OkHttpClient client = new OkHttpClient();
        LocationDto locationDto = locationUtils.getLocation(location);

        Request request = new Request.Builder()
                .url("https://api.foursquare.com/v3/places/search?query="+type+"&ll="+locationDto.getLat()+"%2C"+locationDto.getLon())
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", foursqureApiKey)
                .build();

        Response response = client.newCall(request).execute();

        return new ResponseDto<>(simplifyHotelData(response.body().string()), "Success", "Recommendations for " + location);
    }
    public List<PlaceResponseDto> simplifyHotelData(String rawData) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(rawData);

        List<PlaceResponseDto> hotels = new ArrayList<>();

        // Access the "results" array
        JsonNode results = rootNode.path("results");

        if (results.isArray()) {
            for (JsonNode result : results) {
                PlaceResponseDto place = new PlaceResponseDto();

                place.setName(result.path("name").asText());
                place.setAddress(result.path("location").path("formatted_address").asText());
                place.setLocality(result.path("location").path("locality").asText());
                place.setRegion(result.path("location").path("region").asText());
                place.setCountry(result.path("location").path("country").asText());
                place.setPostcode(result.path("location").path("postcode").asText());

                place.setLatitude(result.path("geocodes").path("main").path("latitude").asDouble());
                place.setLongitude(result.path("geocodes").path("main").path("longitude").asDouble());

                place.setDistance(result.path("distance").asInt());

                // Get the first category
                place.setCategory(result.path("categories").get(0).path("name").asText());

                hotels.add(place);
            }
        }

        return hotels;
    }
}
