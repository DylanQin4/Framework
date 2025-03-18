package mg.itu.avion.city;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class CountryRepository {
    private SqlSessionFactory sqlSessionFactory;
    
    public CountryRepository(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public String getCountryNameById(Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            CountryMapper mapper = session.getMapper(CountryMapper.class);
            return mapper.getCountryById(id).getName();
        }
    }
}
