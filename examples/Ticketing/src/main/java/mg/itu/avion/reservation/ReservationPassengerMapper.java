package mg.itu.avion.reservation;

import org.apache.ibatis.annotations.*;
import java.util.List;

public interface ReservationPassengerMapper {
	@Select("""
		SELECT rp.*,
			cls.label AS class_name,
			pt.type_name  AS passenger_type
		FROM reservation_passengers rp
		LEFT JOIN class           cls ON cls.id = rp.class_id
		LEFT JOIN passenger_type  pt  ON pt.id  = rp.passenger_type_id
		WHERE rp.reservation_id = #{reservationId}
		ORDER BY rp.id
		""")
	@Results({
		@Result(property="id",                 column="id"),
		@Result(property="reservationId",      column="reservation_id"),
		@Result(property="passengerName",      column="passenger_name"),
		@Result(property="passengerBirthdate", column="passenger_birthdate"),
		@Result(property="passengerTypeId",    column="passenger_type_id"),
		@Result(property="passengerType",      column="passenger_type"),   // ← ajouté (nom du type)
		@Result(property="classId",            column="class_id"),
		@Result(property="className",          column="class_name"),
		@Result(property="basePrice",          column="base_price"),
		@Result(property="discount",           column="discount"),
		@Result(property="finalPrice",         column="final_price"),
		@Result(property="promoApplied",       column="promo_applied"),
		@Result(property="filePathPassport",   column="file_path_passport"),
		@Result(property="createdAt",          column="created_at")
	})
	List<ReservationPassenger> findByReservationId(Integer reservationId);

    @Insert("""
        INSERT INTO reservation_passengers (
          reservation_id, passenger_name, passenger_birthdate, passenger_type_id,
          class_id, base_price, discount, final_price, promo_applied, file_path_passport, created_at
        ) VALUES (
          #{reservationId}, #{passengerName}, CAST(#{passengerBirthdate} AS DATE), #{passengerTypeId},
          #{classId}, #{basePrice}, #{discount}, #{finalPrice}, #{promoApplied}, #{filePathPassport}, #{createdAt}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(ReservationPassenger line);

    @Select("""
        SELECT COUNT(*) FROM reservation_passengers rp
        JOIN reservations r ON r.id = rp.reservation_id
        WHERE r.flight_id = #{flightId}
          AND rp.class_id = #{classId}
          AND r.status IN ('RESERVED','PAID','PENDING')
        """)
    int countReservedSeatsByFlightAndClass(@Param("flightId") Integer flightId,
                                           @Param("classId") Integer classId);

    
    @Delete("DELETE FROM reservation_passengers WHERE reservation_id = #{reservationId}")
    void deleteByReservationId(@Param("reservationId") Integer reservationId);

	@Select("""
		SELECT COUNT(*) FROM reservation_passengers rp
		JOIN reservations r ON r.id = rp.reservation_id
		WHERE r.flight_id = #{flightId}
		AND rp.class_id = #{classId}
		AND r.status = 'PAID'
	""")
	int countPaidSeatsByFlightAndClass(@Param("flightId") Integer flightId, @Param("classId") Integer classId);


}