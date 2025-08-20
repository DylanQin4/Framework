package mg.itu.avion.passenger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerType {
    private Integer id;
    private String typeName;
    private Integer startAge;
    private Integer endAge;
}