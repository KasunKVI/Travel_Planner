package software.kasunkavinda.Travel_Planner.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.kasunkavinda.Travel_Planner.dto.HotelDto;
import software.kasunkavinda.Travel_Planner.dto.HotelPhotosDto;
import software.kasunkavinda.Travel_Planner.dto.ResponseDto;
import software.kasunkavinda.Travel_Planner.dto.ReviewResponseDto;
import software.kasunkavinda.Travel_Planner.service.HotelService;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
public class HotelAPI {

    private final HotelService hotelService;

    private final Logger logger = LoggerFactory.getLogger(HotelAPI.class);


    @GetMapping("/health")
    public String health(){
        return "Hotel API is working";
    }

    @GetMapping("/getHotels")
    public ResponseEntity<ResponseDto<List<HotelDto>>> getHotels(@RequestParam String location) {
        logger.info("Request received to get hotels.");
        try {
            return new ResponseEntity<>(hotelService.getNearbyHotels(location), HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getHotelReviews")
    public ResponseEntity<ResponseDto<List<ReviewResponseDto>>> getHotelReviews(@RequestParam String locationId) {
        logger.info("Request received to get hotel reviews.");
        try {
            return new ResponseEntity<>(hotelService.getHotelReviews(locationId), HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getHotelPhotos")
    public ResponseEntity<ResponseDto<List<HotelPhotosDto>>> getHotelPhotos(@RequestParam String locationId) {
        logger.info("Request received to get hotel photos.");
        try {
            return new ResponseEntity<>(hotelService.getHotelPhotos(locationId), HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseDto<HotelDto>> saveHotel(@RequestBody HotelDto hotelDto) {
        logger.info("Request received to save hotel.");
        return new ResponseEntity<>( hotelService.saveHotel(hotelDto), HttpStatus.OK);
    }

    @GetMapping("/getHotel")
    public ResponseEntity<ResponseDto<HotelDto>> getHotel(@RequestParam String locationId) {
        logger.info("Request received to get hotel.");
        return new ResponseEntity<>(hotelService.getHotel(locationId), HttpStatus.OK);
    }

    @DeleteMapping("/deleteHotel")
    public ResponseEntity<ResponseDto<String>> deleteHotel(@RequestParam String locationId) {
        logger.info("Request received to delete hotel.");
        return new ResponseEntity<>(hotelService.deleteHotel(locationId), HttpStatus.OK);
    }


}
