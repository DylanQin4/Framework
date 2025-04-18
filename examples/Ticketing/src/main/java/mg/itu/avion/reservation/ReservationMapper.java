package mg.itu.avion.reservation;

import org.apache.ibatis.annotations.*;
import java.util.List;

public interface ReservationMapper {

    @Select("SELECT * FROM reservations ORDER BY created_at DESC")
    @Results({
        @Result(property="id",           column="id"),
        @Result(property="userId",       column="user_id"),
        @Result(property="flightId",     column="flight_id"),
        @Result(property="status",       column="status"),
        @Result(property="totalAmount",  column="total_amount"),
        @Result(property="totalDiscount",column="total_discount"),
        @Result(property="cancelledAt",  column="cancelled_at"),
        @Result(property="createdAt",    column="created_at"),
        @Result(property="updatedAt",    column="updated_at")
    })
    List<Reservation> getAllReservations();

    @Select("SELECT * FROM reservations WHERE id = #{id}")
    @Results({
        @Result(property="id",           column="id"),
        @Result(property="userId",       column="user_id"),
        @Result(property="flightId",     column="flight_id"),
        @Result(property="status",       column="status"),
        @Result(property="totalAmount",  column="total_amount"),
        @Result(property="totalDiscount",column="total_discount"),
        @Result(property="cancelledAt",  column="cancelled_at"),
        @Result(property="createdAt",    column="created_at"),
        @Result(property="updatedAt",    column="updated_at")
    })
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