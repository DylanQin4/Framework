package mg.itu.avion.airplane;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;

public interface AirplaneClassMapper {
    
    @Select("SELECT seat_count FROM airplane_class WHERE airplane_id = #{airplaneId} AND class_id = #{classId}")
    @Result(property = "seatCount", column = "seat_count")
    Integer getSeatCountByAirplaneIdClassId(
        @Param("airplaneId") int airplaneId, 
        @Param("classId") int classId
    );
}
