package mg.itu.avion.airplane;

import org.apache.ibatis.annotations.*;
import java.util.List;

public interface ClassMapper {

    @Select("SELECT * FROM class")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "label", column = "label")
    })
    List<Class> getAllClasses();

    @Select("SELECT * FROM class WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "label", column = "label")
    })
    Class getClassById(Integer id);
}