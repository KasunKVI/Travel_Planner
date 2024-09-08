package software.kasunkavinda.Travel_Planner.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Flights {

    @Id
    private String flightCode;

    @Column(nullable = false)
    private String departure;
    @Column(nullable = false)
    private String arrival;
    @Column(nullable = false)
    private String departureTime;
    @Column(nullable = false)
    private String arrivalTime;
    @Column(nullable = false)
    private String duration;
    @Column(nullable = false)
    private String price;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "itinerary_id", referencedColumnName = "id")
    private Itineraries itinerary;

}
