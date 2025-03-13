package mg.itu.avion.user;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface RoleMapper {
    @Select("SELECT * FROM roles WHERE label = #{label}")
    Role getRoleByLabel(String label);

    @Select("SELECT r.id, r.label FROM roles r JOIN user_roles ur ON r.id = ur.role_id WHERE ur.user_id = #{userId}")
    List<Role> getRolesByUserId(int userId);

    @Insert("INSERT INTO user_roles (user_id, role_id) VALUES (#{userId}, #{roleId})")
    void saveUserRole(int userId, int roleId);
}
