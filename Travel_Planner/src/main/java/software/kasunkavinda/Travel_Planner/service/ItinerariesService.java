package software.kasunkavinda.Travel_Planner.service;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.kasunkavinda.Travel_Planner.dto.ItinerariesDto;
import software.kasunkavinda.Travel_Planner.dto.ResponseDto;
import software.kasunkavinda.Travel_Planner.entity.Itineraries;
import software.kasunkavinda.Travel_Planner.repository.FlightRepo;
import software.kasunkavinda.Travel_Planner.repository.HotelRepo;
import software.kasunkavinda.Travel_Planner.repository.ItinerariesRepo;
import software.kasunkavinda.Travel_Planner.util.Mapping;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItinerariesService {

    private final ItinerariesRepo itinerariesRepo;
    private final HotelRepo hotelRepo;
    private final FlightRepo flightRepo;
    private final Mapping mapping;
    private final Logger logger = LoggerFactory.getLogger(ItinerariesService.class);

    @Transactional(rollbackFor = Exception.class) // Explicitly handling rollback for any exception
    public ResponseDto<ItinerariesDto> saveItinerary(ItinerariesDto itinerariesDto) {

        Itineraries itinerary = itinerariesRepo.findByIdAndStatus(itinerariesDto.getId(),"active");
        if(itinerary != null){
            return new ResponseDto<>(null, "400", "Itinerary already exists");
        }

        logger.info("Request received to save itinerary.");



            // Convert DTO to Entity and save it
            Itineraries itineraryEntity = mapping.convertToEntity(itinerariesDto, Itineraries.class);
            Itineraries savedItinerary = itinerariesRepo.save(itineraryEntity);

            // Convert saved entity back to DTO
            ItinerariesDto savedItineraryDto = mapping.convertToDto(savedItinerary, ItinerariesDto.class);

            // Return success response
            return new ResponseDto<>(savedItineraryDto, "200", "Itinerary saved successfully");

    }

    public ResponseDto<ItinerariesDto> getItinerary(String id) {
        logger.info("Request received to get itinerary.");

        // Find itinerary by ID
        Itineraries itinerary = itinerariesRepo.findByIdAndStatus(id,"active");

        // If itinerary not found, return error response
        if (itinerary == null) {
            return new ResponseDto<>(null, "400", "Itinerary not found");
        }

        // Convert entity to DTO and return success response
        ItinerariesDto itineraryDto = mapping.convertToDto(itinerary, ItinerariesDto.class);
        return new ResponseDto<>(itineraryDto, "200", "Itinerary found");
    }

    public ResponseDto<String> deleteItinerary(String id) {
        logger.info("Request received to delete itinerary.");

        // Find itinerary by ID
        Itineraries itinerary = itinerariesRepo.findById(id).orElse(null);


        // If itinerary not found, return error response
        if (itinerary == null || !itinerary.getStatus().equals("active")) {
            return new ResponseDto<>(id, "400", "Itinerary already in inactive");
        }
        itinerary.setStatus("inactive");
        // Delete itinerary and return success response
        itinerariesRepo.save(itinerary);
        return new ResponseDto<>(id, "200", "Itinerary deleted successfully");
    }


    public ResponseDto<List<ItinerariesDto>> getAllItineraries() {
        logger.info("Request received to get all itineraries.");

        // Find all itineraries
        List<Itineraries> itineraries = itinerariesRepo.findAllByStatus("active");

        return new ResponseDto<>(mapping.convertToDtoList(itineraries), "200", "All itineraries retrieved successfully");

    }
}
