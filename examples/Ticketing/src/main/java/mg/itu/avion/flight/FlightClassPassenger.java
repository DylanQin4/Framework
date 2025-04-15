package mg.itu.avion.flight;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightClassPassenger {
    private Integer id;
    private Integer flightId;
    private Integer classId;
    private Integer passengerTypeId;
    private Integer promotionLimit;
    private Double promotionDiscount;
    private Double basePrice;
}