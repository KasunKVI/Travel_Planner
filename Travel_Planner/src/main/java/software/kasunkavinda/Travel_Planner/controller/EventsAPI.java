package software.kasunkavinda.Travel_Planner.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.kasunkavinda.Travel_Planner.dto.EventResponseDto;
import software.kasunkavinda.Travel_Planner.dto.ResponseDto;
import software.kasunkavinda.Travel_Planner.service.EventService;


@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventsAPI {

    private final Logger logger = LoggerFactory.getLogger(EventsAPI.class);

    private final EventService eventsService;

    @RequestMapping("/health")
    public String health(){
        return "Events API is working";
    }

    @GetMapping("/getEvents")
    public ResponseEntity<ResponseDto<EventResponseDto>> getEvents(@RequestParam String location) throws JsonProcessingException {
        logger.info("Request received to get events.");
        return new ResponseEntity<>(eventsService.getNearbyEvents(location), HttpStatus.OK);
   }
}
