package mg.itu.avion.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Getter
    private Integer id;
    @Getter
    private String label;
}
