package software.kasunkavinda.Travel_Planner.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.kasunkavinda.Travel_Planner.dto.FlightDetailsDto;
import software.kasunkavinda.Travel_Planner.dto.FlightDto;
import software.kasunkavinda.Travel_Planner.dto.FlightOfferDto;
import software.kasunkavinda.Travel_Planner.dto.ResponseDto;
import software.kasunkavinda.Travel_Planner.service.FlightService;
import software.kasunkavinda.Travel_Planner.util.FlightUtilities;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
@RequiredArgsConstructor
public class FlightAPI {

    private final FlightService flightService;

    private final Logger logger= LoggerFactory.getLogger(FlightAPI.class);

    @GetMapping("/health")
    public String health(){
        return "Flight API is working";
    }

    @GetMapping("/getFlights")
    public ResponseEntity<ResponseDto<List<FlightOfferDto>>> getFlightOffers(@RequestBody FlightDetailsDto flightDetailsDto) {
        logger.info("Request received to get flight offers.");
        List<FlightOfferDto> flightOffers = flightService.getFlightDetails(flightDetailsDto);

        ResponseDto<List<FlightOfferDto>> responseDto = new ResponseDto<>(
                flightOffers,
                "success",
                "Flight offers retrieved successfully"
        );
        logger.info("Flight offers retrieved successfully.");

       return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseDto<FlightDto>> saveFlight(@RequestBody FlightDto flightDto) {
        logger.info("Saving flight details");
        return new ResponseEntity<>(flightService.saveFlightDetails(flightDto), HttpStatus.OK);
    }

    @GetMapping("/getFlight")
    public ResponseEntity<ResponseDto<FlightDto>> getFlight(@RequestParam String flightCode) {
        logger.info("Request received to get flight details.");
       return new ResponseEntity<>(flightService.getFlight(flightCode), HttpStatus.OK);
    }

    @DeleteMapping("/deleteFlight")
    public ResponseEntity<ResponseDto<String>> deleteFlight(@RequestParam String flightCode) {
        logger.info("Request received to delete flight details.");
        return new ResponseEntity<>(flightService.deleteFlight(flightCode), HttpStatus.OK);

    }
}
