package mg.itu.avion.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Getter
    private Integer id;
    @Getter
    private String username;
    @Getter
    private String email;
    private String pwd;
    @Getter
    private String role;

    public User(String username, String email, String pwd, String role) {
        this.username = username;
        this.email = email;
        this.pwd = pwd;
        this.role = role;
    }

}
