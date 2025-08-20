package mg.itu.avion.promotion;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Promotion {
    private Integer id;
    private Integer flightId;
    private Integer classId;
    private Integer promotionLimit;
    private Double  promotionDiscount;
    private Double  basePrice;
    private LocalDateTime deadline;
}
