package mg.itu.avion.flight;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import mg.itu.avion.flight.search.FlightSearchCriteria;

public class FlightService {
    private FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }
    
    public List<Flight> getAllFlights() {
        return flightRepository.getAllFlights();
    }
    
    public Flight getFlightById(Integer id) {
        return flightRepository.getFlightById(id);
    }

    public void createFlight(Flight flight) {
        flightRepository.saveFlight(flight);
    }

    public void updateFlight(Flight flight) {
        flightRepository.editFlight(flight);
    }
    
    public void deleteFlight(String id) {
        flightRepository.deleteFlight(Integer.parseInt(id));
    }

    public List<Flight> searchFlightsAdvanced(FlightSearchCriteria criteria) {
        return flightRepository.searchFlightsAdvanced(criteria);
    }
}
