package mg.itu.avion.passenger;

import org.apache.ibatis.annotations.*;

public interface ConfigFaresMapper {

    @Select("SELECT * FROM config_fares ORDER BY created_at DESC LIMIT 1")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "passengerTypeId", column = "passenger_type_id"),
        @Result(property = "price", column = "price"),
        @Result(property = "createdAt", column = "created_at")
    })
    ConfigFares getLatestConfigFares();

    @Select("SELECT * FROM config_fares WHERE passenger_type_id = #{passengerTypeId} ORDER BY created_at DESC LIMIT 1")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "passengerTypeId", column = "passenger_type_id"),
        @Result(property = "price", column = "price"),
        @Result(property = "createdAt", column = "created_at")
    })
    ConfigFares getLatestConfigFaresByPassengerType(Integer passengerTypeId);
}