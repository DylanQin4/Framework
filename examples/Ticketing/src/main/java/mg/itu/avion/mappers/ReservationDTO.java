package mg.itu.avion.mappers;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ReservationDTO {
    private Integer id;
    private String status;
    private String flightNumber;
    private String departureTime;
    private String arrivalTime;
    private String airplane;
    private String departureCity;
    private String arrivalCity;
    private Double totalAmount;
    private Double totalDiscount;
    private String cancelledAt;
    private String createdAt;
    private List<ReservationPassengerDTO> passengers;
}
