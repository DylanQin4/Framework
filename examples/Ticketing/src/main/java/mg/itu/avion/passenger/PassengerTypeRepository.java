package mg.itu.avion.passenger;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import java.util.List;

public class PassengerTypeRepository {
    private SqlSessionFactory sqlSessionFactory;

    public PassengerTypeRepository(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public List<PassengerType> getAllPassengerTypes() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PassengerTypeMapper mapper = session.getMapper(PassengerTypeMapper.class);
            return mapper.getAllPassengerTypes();
        }
    }

    public PassengerType getPassengerTypeById(Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            PassengerTypeMapper mapper = session.getMapper(PassengerTypeMapper.class);
            return mapper.getPassengerTypeById(id);
        }
    }
}