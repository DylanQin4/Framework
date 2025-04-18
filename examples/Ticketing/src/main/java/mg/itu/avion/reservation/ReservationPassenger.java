package mg.itu.avion.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ReservationPassenger {
    private Integer id;
    private Integer reservationId;

    private String  passengerName;
    private LocalDate passengerBirthdate;
    private Integer passengerTypeId;

    private Integer classId;
    private Double  basePrice;
    private Double  discount;
    private Double  finalPrice;
    private Boolean promoApplied;

    private String  filePathPassport;
    private LocalDateTime createdAt;

    public Date getCreatedAtAsDate() {
        return createdAt == null ? null : Date.from(createdAt.atZone(ZoneId.systemDefault()).toInstant());
    }

    public Date getPassengerBirthdateAsDate() {
        return passengerBirthdate == null ? null
            : Date.from(passengerBirthdate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
