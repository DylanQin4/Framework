package mg.itu.avion.flight;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.itu.avion.airplane.Airplane;
import mg.itu.avion.city.City;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

    public void setDepartureTime(String departureTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        this.departureTime = LocalDateTime.parse(departureTime, formatter);
    }
    public void setArrivalTime(String arrivalTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        this.arrivalTime = LocalDateTime.parse(arrivalTime, formatter);
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }
    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Date getDepartureTimeAsDate() {
        return Date.from(departureTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public Date getArrivalTimeAsDate() {
        return Date.from(arrivalTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
