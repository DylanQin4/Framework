package mg.itu.avion.flight;

import java.util.List;

public class FlightService {
    private FlightRepository flightRepository;
    
    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }
    
    public List<Flight> getAllFlights() {
        return flightRepository.getAllFlights();
    }
    
    public Flight getFlightById(String id) {
        return flightRepository.getFlightById(Integer.parseInt(id));
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
}
