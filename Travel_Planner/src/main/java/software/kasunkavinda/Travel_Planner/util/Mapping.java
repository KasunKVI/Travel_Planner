package software.kasunkavinda.Travel_Planner.util;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import software.kasunkavinda.Travel_Planner.dto.ItinerariesDto;
import software.kasunkavinda.Travel_Planner.entity.Itineraries;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Mapping {

    private final ModelMapper modelMapper;

    public <D, T> D convertToDto(T entity, Class<D> outClass) {
        return modelMapper.map(entity, outClass);
    }

    public <D, T> T convertToEntity(D dto, Class<T> outClass) {
        return modelMapper.map(dto, outClass);
    }

    public List<ItinerariesDto> convertToDtoList(List<Itineraries> itineraries) {
       List<ItinerariesDto> itinerariesDto = itineraries.stream()
               .map(itinerary -> modelMapper.map(itinerary, ItinerariesDto.class))
               .collect(Collectors.toList());
         return itinerariesDto;
    }
}
