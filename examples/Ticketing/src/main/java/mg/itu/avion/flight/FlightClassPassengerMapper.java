package mg.itu.avion.flight;

import org.apache.ibatis.annotations.*;
import java.util.List;

public interface FlightClassPassengerMapper {

    @Insert("INSERT INTO flight_class_passenger (flight_id, class_id, passenger_type_id, promotion_limit, promotion_discount, base_price) " +
            "VALUES (#{flightId}, #{classId}, #{passengerTypeId}, #{promotionLimit}, #{promotionDiscount}, #{basePrice})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertFlightClassPassenger(FlightClassPassenger flightClassPassenger);

    @Select("SELECT * FROM flight_class_passenger WHERE flight_id = #{flightId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "flightId", column = "flight_id"),
            @Result(property = "classId", column = "class_id"),
            @Result(property = "passengerTypeId", column = "passenger_type_id"),
            @Result(property = "promotionLimit", column = "promotion_limit"),
            @Result(property = "promotionDiscount", column = "promotion_discount"),
            @Result(property = "basePrice", column = "base_price")
    })
    List<FlightClassPassenger> getFlightClassPassengersByFlightId(Integer flightId);

    @Delete("DELETE FROM flight_class_passenger WHERE flight_id = #{id}")
    @Options(flushCache = Options.FlushCachePolicy.TRUE)
    void deleteFlightClassPassengersByFlightId(Integer id);
}