package software.kasunkavinda.Travel_Planner.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.kasunkavinda.Travel_Planner.dto.PlaceResponseDto;
import software.kasunkavinda.Travel_Planner.dto.ResponseDto;
import software.kasunkavinda.Travel_Planner.service.RecommendationService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
public class RecommendationAPI {

    private final RecommendationService recommendationService;

    Logger logger = LoggerFactory.getLogger(RecommendationAPI.class);

    @GetMapping("/health")
    public String health(){
        return "Recommendation API is working";
    }

    @GetMapping("/getRecommendations")
    public ResponseEntity<ResponseDto<List<PlaceResponseDto>>> getRecommendations(@RequestParam String type,String location)  {
        logger.info("Request received to get recommendations.");
        try {
            return new ResponseEntity<>(recommendationService.getRecommendations(type, location), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
