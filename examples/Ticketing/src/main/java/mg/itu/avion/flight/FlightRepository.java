package mg.itu.avion.flight;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import mg.itu.avion.airplane.AirplaneRepository;
import mg.itu.avion.city.CityRepository;
import mg.itu.avion.flight.search.FlightSearchCriteria;

public class FlightRepository {
    private SqlSessionFactory sqlSessionFactory;
    
    public FlightRepository(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }
    
    public List<Flight> getAllFlights() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            FlightMapper mapper = session.getMapper(FlightMapper.class);
            List<Flight> flights = mapper.getAllFlights();
            for (Flight flight : flights) {
                flight.setDepartureCity(new CityRepository(sqlSessionFactory).getCityById(flight.getDepartureCityId()));
                flight.setArrivalCity(new CityRepository(sqlSessionFactory).getCityById(flight.getArrivalCityId()));
                flight.setAirplane(new AirplaneRepository(sqlSessionFactory).getAirplaneById(flight.getAirplaneId()));
            }
            return flights;
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

    public List<Flight> searchFlightsAdvanced(FlightSearchCriteria criteria) {
        try (SqlSession s = sqlSessionFactory.openSession()) {
            FlightMapper m = s.getMapper(FlightMapper.class);
            return m.searchFlightsAdvanced(criteria);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Return an empty list in case of error
        }
    }
}
