package mg.itu.avion.reservation;

import org.apache.ibatis.annotations.*;
import java.util.List;

public interface ReservationMapper {

    @Select("SELECT * FROM reservations ORDER BY created_at DESC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "flightId", column = "flight_id"),
        @Result(property = "status", column = "status"),
        @Result(property = "amount", column = "amount"),
        @Result(property = "discount", column = "discount"),
        @Result(property = "passengerName", column = "passenger_name"),
        @Result(property = "passengerBirthdate", column = "passenger_birthdate"),
        @Result(property = "filePathPassport", column = "file_path_passport"),
        @Result(property = "classId", column = "class_id"),
        @Result(property = "cancellationDate", column = "cancellation_date"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    List<Reservation> getAllReservations();

    @Insert("INSERT INTO reservations (" +
        "user_id, flight_id, status, amount, discount, " +
        "passenger_name, passenger_birthdate, file_path_passport, class_id, " +
        "cancellation_date, created_at, updated_at) " +
        "VALUES (" +
        "#{userId}, #{flightId}, #{status}, #{amount}, #{discount}, " +
        "#{passengerName}, CAST(#{passengerBirthdate} AS DATE), #{filePathPassport}, #{classId}, " +
        "#{cancellationDate}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertReservation(Reservation reservation);
}