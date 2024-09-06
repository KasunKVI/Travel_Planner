package software.kasunkavinda.Travel_Planner.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.kasunkavinda.Travel_Planner.dto.ResponseDto;
import software.kasunkavinda.Travel_Planner.dto.WeatherDto;
import software.kasunkavinda.Travel_Planner.service.WeatherService;
import software.kasunkavinda.Travel_Planner.util.WeatherUtils;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
public class WeatherAPI  {

    private final WeatherService weatherService;

    private final WeatherUtils weatherUtils;

    @GetMapping("/getWeather")
    private ResponseDto<WeatherDto> getWeather(@RequestParam String location){
        return new ResponseDto<WeatherDto>( weatherService.getWeather(location),"Sucess", "Weather details for "+location);
    }

    @GetMapping("/health")
    public String health(){
        return "Weather API is working";
    }

    @GetMapping("/getLocId")
    public String getLocId(@RequestParam String location){
        return weatherUtils.getLocationId(location);
    }

}
