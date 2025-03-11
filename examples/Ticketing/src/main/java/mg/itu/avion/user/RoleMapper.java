package mg.itu.avion.user;

import java.util.List;

import org.apache.ibatis.annotations.Select;

public interface RoleMapper {
    @Select("SELECT * FROM roles WHERE id = #{id}")
    Role getRoleById(int id);

    @Select("SELECT r.id, r.label FROM roles r JOIN user_roles ur ON r.id = ur.role_id WHERE ur.user_id = #{userId}")
    List<Role> getRolesByUserId(int userId);
}
