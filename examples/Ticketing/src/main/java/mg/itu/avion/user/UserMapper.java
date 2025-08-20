package mg.itu.avion.user;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    @Select("SELECT * FROM users WHERE id = #{id}")
    User getUserById(int id);

    @Select("SELECT id, email, username, pwd FROM users WHERE email = #{email} AND pwd = #{password}")
    User findByEmailAndPassword(@Param("email") String email, @Param("password") String password);    

    @Insert("INSERT INTO users (email, username, pwd) VALUES (#{email}, #{username}, #{pwd})")
    void saveUser(UserRequest userRequest);

    @Select("SELECT * FROM users WHERE email = #{email} OR username = #{username}")
    User findByEmailOrUsername(String email, String username);
}

