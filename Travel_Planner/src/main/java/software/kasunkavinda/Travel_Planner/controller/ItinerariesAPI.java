package software.kasunkavinda.Travel_Planner.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.kasunkavinda.Travel_Planner.dto.ItinerariesDto;
import software.kasunkavinda.Travel_Planner.dto.ResponseDto;
import software.kasunkavinda.Travel_Planner.service.ItinerariesService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/itineraries")
@RequiredArgsConstructor
public class ItinerariesAPI {

    private final ItinerariesService itinerariesService;
    private final Logger logger = LoggerFactory.getLogger(ItinerariesAPI.class);

    @GetMapping("/health")
    public String health(){
        return "Itineraries API is working";
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseDto<ItinerariesDto>> saveItinerary(@RequestBody ItinerariesDto itinerariesDto) {
        logger.info("Request received to save itinerary.");
        return new ResponseEntity<>(itinerariesService.saveItinerary(itinerariesDto), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<ResponseDto<ItinerariesDto>> getItinerary(@RequestParam String id) {
        logger.info("Request received to get itinerary.");
        return new ResponseEntity<>(itinerariesService.getItinerary(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto<String>> deleteItinerary(@RequestParam String id) {
        logger.info("Request received to delete itinerary.");
        return new ResponseEntity<>(itinerariesService.deleteItinerary(id), HttpStatus.OK);
    }

    @GetMapping("/getall")
    public ResponseEntity<ResponseDto<List<ItinerariesDto>>> getAllItineraries() {
        logger.info("Request received to get all itineraries.");
        return  new ResponseEntity<>(itinerariesService.getAllItineraries(), HttpStatus.OK);
    }
}
