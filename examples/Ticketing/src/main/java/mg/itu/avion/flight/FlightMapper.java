package mg.itu.avion.flight;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import mg.itu.avion.airplane.Airplane;
import mg.itu.avion.city.City;

import java.util.List;

public interface FlightMapper {

    @Select("SELECT * FROM flights ORDER BY departure_time")
    @Results(id="FlightWithRefs", value = {
        @Result(property="id",                       column="id"),
        @Result(property="flightNumber",            column="flight_number"),
        @Result(property="departureTime",           column="departure_time"),
        @Result(property="arrivalTime",             column="arrival_time"),
        @Result(property="reservationCutoffHours",  column="reservation_cutoff_hours"),
        @Result(property="cancellationCutoffHours", column="cancellation_cutoff_hours"),
        @Result(property="airplaneId",              column="airplane_id"),
        @Result(property="departureCityId",         column="departure_city_id"),
        @Result(property="arrivalCityId",           column="arrival_city_id"),
        @Result(property="createdAt",               column="created_at"),

        // references to other entities
        @Result(property="departureCity", column="departure_city_id",
                javaType=City.class,
                one=@One(select="mg.itu.avion.city.CityMapper.getCityById", fetchType=FetchType.LAZY)),
        @Result(property="arrivalCity", column="arrival_city_id",
                javaType=City.class,
                one=@One(select="mg.itu.avion.city.CityMapper.getCityById", fetchType=FetchType.LAZY)),
        @Result(property="airplane", column="airplane_id",
                javaType=Airplane.class,
                one=@One(select="mg.itu.avion.airplane.AirplaneMapper.getAirplaneById", fetchType=FetchType.LAZY))
    })
    List<Flight> getAllFlights();

    @Select("SELECT * FROM flights WHERE id = #{id}")
    @ResultMap("FlightWithRefs")
    Flight getFlightById(Integer id);

    @Insert("INSERT INTO flights (flight_number, departure_time, arrival_time, reservation_cutoff_hours, " +
            "cancellation_cutoff_hours, airplane_id, departure_city_id, arrival_city_id, created_at) " +
            "VALUES (#{flightNumber}, #{departureTime}, #{arrivalTime}, #{reservationCutoffHours}, " +
            "#{cancellationCutoffHours}, #{airplaneId}, #{departureCityId}, #{arrivalCityId}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertFlight(Flight flight);

    @Update("UPDATE flights SET " +
            "flight_number = #{flightNumber}, " +
            "departure_time = #{departureTime}, " +
            "arrival_time = #{arrivalTime}, " +
            "reservation_cutoff_hours = #{reservationCutoffHours}, " +
            "cancellation_cutoff_hours = #{cancellationCutoffHours}, " +
            "airplane_id = #{airplaneId}, " +
            "departure_city_id = #{departureCityId}, " +
            "arrival_city_id = #{arrivalCityId} " +
            "WHERE id = #{id}")
    void updateFlight(Flight flight);

    @Delete("DELETE FROM flights WHERE id = #{id}")
    void deleteFlight(Integer id);
}
