package software.kasunkavinda.Travel_Planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import software.kasunkavinda.Travel_Planner.entity.Hotels;

public interface HotelRepo extends JpaRepository<Hotels, String> {
}
