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

    public void deleteFlightClassPassengersByFlightId(Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            FlightClassPassengerMapper mapper = session.getMapper(FlightClassPassengerMapper.class);
            mapper.deleteFlightClassPassengersByFlightId(id);
            session.commit();
        }
    }

    public FlightClassPassenger getFlightClassPassengerById(Integer flightId, Integer classId, Integer passengerTypeId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            FlightClassPassengerMapper mapper = session.getMapper(FlightClassPassengerMapper.class);
            return mapper.getFlightClassPassengerById(flightId, classId, passengerTypeId);
        }
    }
}