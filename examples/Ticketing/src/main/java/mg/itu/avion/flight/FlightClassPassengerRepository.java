package mg.itu.avion.flight;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import java.util.List;

public class FlightClassPassengerRepository {
    private SqlSessionFactory sqlSessionFactory;

    public FlightClassPassengerRepository(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public void saveFlightClassPassenger(FlightClassPassenger flightClassPassenger) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            FlightClassPassengerMapper mapper = session.getMapper(FlightClassPassengerMapper.class);
            mapper.insertFlightClassPassenger(flightClassPassenger);
            session.commit();
        }
    }

    public List<FlightClassPassenger> getFlightClassPassengersByFlightId(Integer flightId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            FlightClassPassengerMapper mapper = session.getMapper(FlightClassPassengerMapper.class);
            return mapper.getFlightClassPassengersByFlightId(flightId);
        }
    }
}