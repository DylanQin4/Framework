package mg.itu.avion.airplane;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirplaneClass {
    private Integer airplaneId;
    private Integer classId;
    private Integer seatCount;
}
