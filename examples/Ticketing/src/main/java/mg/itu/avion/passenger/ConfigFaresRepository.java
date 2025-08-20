package mg.itu.avion.passenger;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class ConfigFaresRepository {
    private SqlSessionFactory sqlSessionFactory;

    public ConfigFaresRepository(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public ConfigFares getLatestConfigFares() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ConfigFaresMapper mapper = session.getMapper(ConfigFaresMapper.class);
            return mapper.getLatestConfigFares();
        }
    }

    public ConfigFares getLatestConfigFaresByPassengerType(Integer passengerTypeId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            ConfigFaresMapper mapper = session.getMapper(ConfigFaresMapper.class);
            return mapper.getLatestConfigFaresByPassengerType(passengerTypeId);
        }
    }
}