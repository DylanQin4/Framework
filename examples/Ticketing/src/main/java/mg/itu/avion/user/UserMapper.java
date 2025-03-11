package mg.itu.avion.user;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    @Select("SELECT * FROM users WHERE id = #{id}")
    User getUserById(int id);

    @Select("SELECT id, email, username, pwd FROM users WHERE email = #{email} AND pwd = #{password}")
    User findByEmailAndPassword(@Param("email") String email, @Param("password") String password);    

    // Tu peux ajouter d'autres méthodes annotées (@Insert, @Update, @Delete) selon tes besoins
}

