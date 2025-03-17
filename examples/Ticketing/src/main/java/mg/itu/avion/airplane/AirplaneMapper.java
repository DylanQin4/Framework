package mg.itu.avion.airplane;

import org.apache.ibatis.annotations.*;
import java.util.List;

public interface AirplaneMapper {

    @Select("SELECT * FROM airplanes ORDER BY model")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "model", column = "model"),
        @Result(property = "totalSeats", column = "total_seats")
    })
    List<Airplane> getAllAirplanes();

    @Select("SELECT * FROM airplanes WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "model", column = "model"),
        @Result(property = "totalSeats", column = "total_seats")
    })
    Airplane getAirplaneById(Integer id);
}
