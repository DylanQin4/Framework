package mg.itu.avion.city;

import org.apache.ibatis.annotations.*;
import java.util.List;

public interface CityMapper {

    @Select("SELECT * FROM cities ORDER BY name")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "countryId", column = "country_id")
    })
    List<City> getAllCities();

    @Select("SELECT * FROM cities WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "countryId", column = "country_id")
    })
    City getCityById(Integer id);
    
    @Select("SELECT * FROM cities WHERE country_id = #{countryId} ORDER BY name")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "countryId", column = "country_id")
    })
    List<City> getCitiesByCountryId(Integer countryId);
}
