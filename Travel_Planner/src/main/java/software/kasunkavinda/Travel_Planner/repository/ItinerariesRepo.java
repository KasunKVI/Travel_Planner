package software.kasunkavinda.Travel_Planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import software.kasunkavinda.Travel_Planner.entity.Itineraries;

import java.util.List;

public interface ItinerariesRepo extends JpaRepository<Itineraries, String> {

    @Query("SELECT i FROM Itineraries i WHERE i.id = :id AND i.status = :status")
    Itineraries findByIdAndStatus(@Param("id") String id, @Param("status") String status);

    @Query("SELECT i FROM Itineraries i WHERE i.status = :status")
    List<Itineraries> findAllByStatus(@Param("status") String status);

}
