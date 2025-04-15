package mg.itu.avion.reservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.ETU1792.annotation.validation.Numeric;
import com.ETU1792.annotation.validation.Required;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private Double amount;
    @Numeric
    private Double discount;
    @Required
    private String passengerName;
    @Required
    private LocalDate passengerBirthdate;
    @Required
    private String filePathPassport; 
    @Required
    private Integer classId;
    private LocalDateTime cancellationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}