package mg.itu.avion.user;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
public class User {
    @Getter
    private Integer id;
    @Getter
    private String username;
    @Getter
    private String email;
    private String pwd;
    @Getter
    @Setter
    private List<Role> roles = new ArrayList<>();

    public User(Integer id, String username, String email, String pwd) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.pwd = pwd;
    }

    public List<String> getRolesLabel() {
        List<String> rolesLabel = new ArrayList<>();
        for (Role role : roles) {
            rolesLabel.add(role.getLabel());
        }
        return rolesLabel;
    }

}
