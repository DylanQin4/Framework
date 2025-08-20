package mg.itu.avion.mappers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ReservationPassengerDTO {
    private String passengerName;
    private String passengerBirthdate;
    private String passengerType;
    private String className;
    private Double basePrice;
    private Double discount;
    private Double finalPrice;
    private Boolean promoApplied;
}
