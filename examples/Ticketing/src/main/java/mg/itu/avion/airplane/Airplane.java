package mg.itu.avion.airplane;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Airplane {
    private Integer id;
    private String model;
    private Integer totalSeats;
}
