package mg.itu.avion.flight;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {
    private Integer id;
    private String flightNumber;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer reservationCutoffHours;
    private Integer cancellationCutoffHours;
    private Integer airplaneId;
    private Integer departureCityId;
    private Integer arrivalCityId;
    private LocalDateTime createdAt;

    public Date getDepartureTimeAsDate() {
        return Date.from(departureTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public Date getArrivalTimeAsDate() {
        return Date.from(arrivalTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
