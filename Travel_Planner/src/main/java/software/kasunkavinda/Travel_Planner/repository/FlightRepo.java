package software.kasunkavinda.Travel_Planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import software.kasunkavinda.Travel_Planner.entity.Flights;

public interface FlightRepo extends JpaRepository<Flights, String> {

}
