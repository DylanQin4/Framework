package mg.itu.avion.flight;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.itu.avion.airplane.Airplane;
import mg.itu.avion.city.City;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.ETU1792.annotation.validation.Required;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {
    private Integer id;
    @Required
    private String flightNumber;
    @Required
    private LocalDateTime departureTime;
    @Required
    private LocalDateTime arrivalTime;
    @Required
    private Integer reservationCutoffHours;
    @Required
    private Integer cancellationCutoffHours;
    @Required
    private Integer airplaneId;
    @Required
    private Integer departureCityId;
    @Required
    private Integer arrivalCityId;
    private LocalDateTime createdAt;

    private City departureCity;
    private City arrivalCity;
    private Airplane airplane;

    public Date getDepartureTimeAsDate() {
        return Date.from(departureTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public Date getArrivalTimeAsDate() {
        return Date.from(arrivalTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
