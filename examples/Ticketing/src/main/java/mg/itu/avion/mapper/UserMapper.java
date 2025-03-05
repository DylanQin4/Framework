package mg.itu.avion.mapper;

import mg.itu.avion.entity.User;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    @Select("SELECT id, username, email FROM users WHERE id = #{id}")
    User getUserById(int id);

    // Tu peux ajouter d'autres méthodes annotées (@Insert, @Update, @Delete) selon tes besoins
}

