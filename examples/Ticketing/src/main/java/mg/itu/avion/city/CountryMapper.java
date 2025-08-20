package mg.itu.avion.city;

import org.apache.ibatis.annotations.*;
import java.util.List;

public interface CountryMapper {

    @Select("SELECT * FROM country ORDER BY name")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name")
    })
    List<Country> getAllCountries();

    @Select("SELECT * FROM country WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name")
    })
    Country getCountryById(Integer id);
}
