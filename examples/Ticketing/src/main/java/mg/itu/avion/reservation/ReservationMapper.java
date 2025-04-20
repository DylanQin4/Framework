package mg.itu.avion.reservation;

import org.apache.ibatis.annotations.*;
import java.util.List;

public interface ReservationMapper {

    @Results(id = "ReservationMap", value = {
        @Result(property="id",            column="id"),
        @Result(property="userId",        column="user_id"),
        @Result(property="flightId",      column="flight_id"),
        @Result(property="status",        column="status"),
        @Result(property="totalAmount",   column="total_amount"),
        @Result(property="totalDiscount", column="total_discount"),
        @Result(property="cancelledAt",   column="cancelled_at"),
        @Result(property="createdAt",     column="created_at"),
        @Result(property="updatedAt",     column="updated_at"),

        // Champs dérivés
        @Result(property="flightNumber",  column="flight_number"),
		@Result(property="departureTime", column="departure_time"),
		@Result(property="arrivalTime",   column="arrival_time"),
        @Result(property="airplane",      column="airplane"),
        @Result(property="departureCity", column="departure_city"),
        @Result(property="arrivalCity",   column="arrival_city")
    })
    @Select("""
            SELECT r.*,
                   f.flight_number,
				   f.departure_time,
				   f.arrival_time,
                   ap.model       AS airplane,
                   dep.name       AS departure_city,
                   arr.name       AS arrival_city
            FROM reservations r
            LEFT JOIN flights    f   ON f.id   = r.flight_id
            LEFT JOIN airplanes  ap  ON ap.id  = f.airplane_id
            LEFT JOIN cities     dep ON dep.id = f.departure_city_id
            LEFT JOIN cities     arr ON arr.id = f.arrival_city_id
            ORDER BY r.created_at DESC
            """)
    List<Reservation> getAllReservations();

    @Select("""
            SELECT r.*,
                   f.flight_number,
				   f.departure_time,
				   f.arrival_time,
                   ap.model       AS airplane,
                   dep.name       AS departure_city,
                   arr.name       AS arrival_city
            FROM reservations r
            LEFT JOIN flights    f   ON f.id   = r.flight_id
            LEFT JOIN airplanes  ap  ON ap.id  = f.airplane_id
            LEFT JOIN cities     dep ON dep.id = f.departure_city_id
            LEFT JOIN cities     arr ON arr.id = f.arrival_city_id
            WHERE r.id = #{id}
            """)
    @ResultMap("ReservationMap")
    Reservation getReservationById(Integer id);

    @Insert("""
        INSERT INTO reservations (
          user_id, flight_id, status, total_amount, total_discount,
          cancelled_at, created_at, updated_at
        ) VALUES (
          #{userId}, #{flightId}, #{status}, #{totalAmount}, #{totalDiscount},
          #{cancelledAt}, #{createdAt}, #{updatedAt}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertReservation(Reservation reservation);

    @Update("""
        UPDATE reservations SET
          user_id = #{userId},
          flight_id = #{flightId},
          status = #{status},
          total_amount = #{totalAmount},
          total_discount = #{totalDiscount},
          cancelled_at = #{cancelledAt},
          updated_at = #{updatedAt}
        WHERE id = #{id}
        """)
    void updateReservation(Reservation reservation);
}