package software.kasunkavinda.Travel_Planner.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import software.kasunkavinda.Travel_Planner.dto.AddressDto;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Hotels {

    @Id
    private String location_id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private double distance;
    @Column(nullable = false)
    private String bearing;
    @Column(nullable = false)
    private String address;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "itinerary_id", referencedColumnName = "id")
    private Itineraries itinerary;

}
