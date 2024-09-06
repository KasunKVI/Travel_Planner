package software.kasunkavinda.Travel_Planner.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import software.kasunkavinda.Travel_Planner.dto.LocationDto;
import software.kasunkavinda.Travel_Planner.dto.WeatherDto;
import software.kasunkavinda.Travel_Planner.util.WeatherUtils;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WeatherService {

    @Value("${openweather.api.key}")
    private String apiKey;

    private final WeatherUtils weatherUtils;

    private final RestTemplate restTemplate;

    private final Gson gson;

    private final String baseUrl = "https://api.openweathermap.org/data/2.5/weather?";

    Logger logger = LoggerFactory.getLogger(WeatherService.class);

    public LocationDto getLocation(String location){
        logger.info("Getting location for {}", location);
        return weatherUtils.getLocation(location);
    }

    public WeatherDto getWeather(String location) {
        logger.info("Getting weather for {}", location);
        LocationDto locationDto = getLocation(location);
        String url = baseUrl + "lat=" + locationDto.getLat() + "&lon=" + locationDto.getLon() + "&appid=" + apiKey + "&units=metric";

        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String responseBody = Objects.requireNonNull(response.getBody());

        // Parse the response to get relevant data
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

        String locationName = jsonObject.get("name").getAsString();
        String country = jsonObject.getAsJsonObject("sys").get("country").getAsString();
        JsonObject weatherObject = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject();
        String weatherMain = weatherObject.get("main").getAsString();
        String weatherDescription = weatherObject.get("description").getAsString();
        String icon = weatherObject.get("icon").getAsString();

        JsonObject main = jsonObject.getAsJsonObject("main");
        double temp = main.get("temp").getAsDouble();
        double feelsLike = main.get("feels_like").getAsDouble();
        double tempMin = main.get("temp_min").getAsDouble();
        double tempMax = main.get("temp_max").getAsDouble();
        int pressure = main.get("pressure").getAsInt();
        int humidity = main.get("humidity").getAsInt();

        JsonObject wind = jsonObject.getAsJsonObject("wind");
        double windSpeed = wind.get("speed").getAsDouble();
        int windDeg = wind.get("deg").getAsInt();
        double windGust = wind.has("gust") ? wind.get("gust").getAsDouble() : 0;

        int visibility = jsonObject.get("visibility").getAsInt();

        // Map the data into WeatherDto

        return new WeatherDto(
                locationName,
                country,
                weatherMain,
                weatherDescription,
                temp,
                feelsLike,
                tempMin,
                tempMax,
                pressure,
                humidity,
                windSpeed,
                windGust,
                windDeg,
                visibility,
                icon
        );
    }
}
