package mg.itu.avion.promotion;

import org.apache.ibatis.annotations.*;
import java.time.LocalDateTime;
import java.util.List;

public interface PromotionMapper {

    @Select("SELECT * FROM promotion WHERE deadline <= #{before} ORDER BY deadline ASC")
    @Results(id = "PromotionMap", value = {
        @Result(property = "id",                column = "id"),
        @Result(property = "flightId",          column = "flight_id"),
        @Result(property = "classId",           column = "class_id"),
        @Result(property = "promotionLimit",    column = "promotion_limit"),
        @Result(property = "promotionDiscount", column = "promotion_discount"),
        @Result(property = "basePrice",         column = "base_price"),
        @Result(property = "deadline",          column = "deadline")
    })
    List<Promotion> findPromotionsBefore(@Param("before") LocalDateTime before);

    @Select("""
        SELECT COUNT(*) FROM reservations r
        JOIN reservation_passengers rp ON r.id = rp.reservation_id
        WHERE r.flight_id = #{flightId}
          AND rp.class_id = #{classId}
          AND r.status = 'RESERVED'
    """)
    int countUnpaidReservations(@Param("flightId") Integer flightId, @Param("classId") Integer classId);

    @Update("""
        UPDATE promotion
        SET promotion_limit = #{promotionLimit}
        WHERE id = #{id}
    """)
    void updatePromotion(Promotion promotion);
}
