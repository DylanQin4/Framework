package mg.itu.avion.city;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class CityRepository {
    private SqlSessionFactory sqlSessionFactory;
    
    public CityRepository(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }
    
    public List<City> getAllCities() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            CityMapper mapper = session.getMapper(CityMapper.class);
            return mapper.getAllCities();
        }
    }
    
    public City getCityById(Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            CityMapper mapper = session.getMapper(CityMapper.class);
            return mapper.getCityById(id);
        }
    }
    
    public List<City> getCitiesByCountryId(Integer countryId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            CityMapper mapper = session.getMapper(CityMapper.class);
            return mapper.getCitiesByCountryId(countryId);
        }
    }

}
