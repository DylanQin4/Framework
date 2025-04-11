package mg.itu.avion.flight;

import org.apache.ibatis.annotations.*;
import java.util.List;

public interface FlightClassPassengerMapper {

    @Insert("INSERT INTO flight_class_passenger (flight_id, class_id, passenger_type_id, promotion_limit, promotion_discount, base_price) " +
            "VALUES (#{flightId}, #{classId}, #{passengerTypeId}, #{promotionLimit}, #{promotionDiscount}, #{basePrice})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertFlightClassPassenger(FlightClassPassenger flightClassPassenger);

    @Select("SELECT * FROM flight_class_passenger WHERE flight_id = #{flightId}")
    List<FlightClassPassenger> getFlightClassPassengersByFlightId(Integer flightId);
}