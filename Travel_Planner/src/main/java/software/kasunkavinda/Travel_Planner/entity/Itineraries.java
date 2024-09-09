package software.kasunkavinda.Travel_Planner.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Itineraries {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private String startDate;

    @Column(nullable = false)
    private String endDate;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean isInternal;

    @Column(nullable = false)
    private String status;

    // One-to-Many relationship with Flights
    @OneToMany(mappedBy = "itinerary", fetch = FetchType.EAGER)
    private List<Flights> flights;

    // One-to-Many relationship with Hotels
    @OneToMany(mappedBy = "itinerary", fetch = FetchType.EAGER)
    private List<Hotels> hotels;


}
