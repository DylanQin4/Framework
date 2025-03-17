package mg.itu.avion.flight;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class FlightRepository {
    private SqlSessionFactory sqlSessionFactory;
    
    public FlightRepository(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }
    
    public List<Flight> getAllFlights() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            FlightMapper mapper = session.getMapper(FlightMapper.class);
            return mapper.getAllFlights();
        }
    }
    
    public Flight getFlightById(Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            FlightMapper mapper = session.getMapper(FlightMapper.class);
            return mapper.getFlightById(id);
        }
    }
    
    public void saveFlight(Flight flight) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            FlightMapper mapper = session.getMapper(FlightMapper.class);
            mapper.insertFlight(flight);
            session.commit();
        }
    }
    
    public void editFlight(Flight flight) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            FlightMapper mapper = session.getMapper(FlightMapper.class);
            mapper.updateFlight(flight);
            session.commit();
        }
    }
    
    public void deleteFlight(Integer id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            FlightMapper mapper = session.getMapper(FlightMapper.class);
            mapper.deleteFlight(id);
            session.commit();
        }
    }
}
