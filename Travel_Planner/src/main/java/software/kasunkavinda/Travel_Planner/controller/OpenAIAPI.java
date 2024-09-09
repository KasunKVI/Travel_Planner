package software.kasunkavinda.Travel_Planner.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.kasunkavinda.Travel_Planner.dto.RecommendationDto;
import software.kasunkavinda.Travel_Planner.dto.ResponseDto;
import software.kasunkavinda.Travel_Planner.service.OpenAIService;

@RestController
@RequestMapping("/api/v1/openAI")
@RequiredArgsConstructor
public class OpenAIAPI {

    private final OpenAIService openAIService;
    private final Logger logger= LoggerFactory.getLogger(OpenAIAPI.class);

    @GetMapping("/health")
    public String health(){
        return "Recommendation API is working";
    }

    @GetMapping("/getRecommendations")
    public ResponseEntity<ResponseDto<String>> getRecommendations(@RequestBody RecommendationDto recommendationDto) {
        logger.info("Request received to get recommendations.");
        try {
            return new ResponseEntity<>(openAIService.getSuggestions(recommendationDto.getOrigin(), recommendationDto.getDestination()), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/chatBot")
    public ResponseEntity<ResponseDto<String>> chatBot(@RequestParam String chat) {
        logger.info("Request received to get chat.");
        try {
            return new ResponseEntity<>(openAIService.chatBot(chat), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }



}
