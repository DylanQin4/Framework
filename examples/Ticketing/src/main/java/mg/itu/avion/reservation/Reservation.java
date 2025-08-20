package mg.itu.avion.reservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.ETU1792.annotation.validation.Numeric;
import com.ETU1792.annotation.validation.Required;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Reservation {
    private Integer id;
    private Integer userId;
    @Required
    private Integer flightId;
    private ReservationStatus status;
    @Numeric
    private Double TotalAmount;
    @Numeric
    private Double TotalDiscount;
    private LocalDateTime cancelledAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<ReservationPassenger> passengers;
    private String flightNumber;
    private String departureTime;
    private String arrivalTime;
    private String airplane;
    private String departureCity;
    private String arrivalCity;

    public Date getCreatedAtAsDate() {
        return createdAt == null ? null : Date.from(createdAt.atZone(ZoneId.systemDefault()).toInstant());
    }
    public Date getUpdatedAtAsDate() {
        return updatedAt == null ? null : Date.from(updatedAt.atZone(ZoneId.systemDefault()).toInstant());
    }
    public Date getCancelledAtAsDate() {
        return cancelledAt == null ? null : Date.from(cancelledAt.atZone(ZoneId.systemDefault()).toInstant());
    }
}