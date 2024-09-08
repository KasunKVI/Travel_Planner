package software.kasunkavinda.Travel_Planner.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.kasunkavinda.Travel_Planner.dto.ResponseDto;
import software.kasunkavinda.Travel_Planner.dto.WeatherDto;
import software.kasunkavinda.Travel_Planner.service.WeatherService;
import software.kasunkavinda.Travel_Planner.util.LocationUtils;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
public class WeatherAPI  {

    private final WeatherService weatherService;

    private final LocationUtils weatherUtils;

    @GetMapping("/getWeather")
    private ResponseEntity<ResponseDto<WeatherDto>> getWeather(@RequestParam String location){
        return new ResponseEntity<>( weatherService.getWeather(location), HttpStatus.OK);
    }

    @GetMapping("/health")
    public String health(){
        return "Weather API is working";
    }


}
