package mg.itu.avion.airplane;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class AirplaneRepository {
    private SqlSessionFactory sqlSessionFactory;
    
    public AirplaneRepository(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }
    
    public List<Airplane> getAllAirplanes() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AirplaneMapper mapper = session.getMapper(AirplaneMapper.class);
            return mapper.getAllAirplanes();
        }
    }
    
    public Airplane getAirplaneById(Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AirplaneMapper mapper = session.getMapper(AirplaneMapper.class);
            return mapper.getAirplaneById(id);
        }
    }
}
