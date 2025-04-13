package mg.itu.avion.passenger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigFares {
    private Integer id;
    private Integer passengerTypeId;
    private Double price;
    private LocalDateTime createdAt;
}