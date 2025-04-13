package mg.itu.avion.passenger;

import org.apache.ibatis.annotations.*;
import java.util.List;

public interface PassengerTypeMapper {

    @Select("SELECT * FROM passenger_type")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "typeName", column = "type_name"),
        @Result(property = "startAge", column = "start_age"),
        @Result(property = "endAge", column = "end_age")
    })
    List<PassengerType> getAllPassengerTypes();

    @Select("SELECT * FROM passenger_type WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "typeName", column = "type_name")
    })
    PassengerType getPassengerTypeById(Integer id);
}