package mg.itu.avion.flight;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    
    public void createFlight(String flightNumber, String departureTime, String arrivalTime, 
                             Integer reservationCutoffHours, Integer cancellationCutoffHours,
                             Integer airplaneId, Integer departureCityId, Integer arrivalCityId) {
        
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        
        Flight flight = new Flight();
        flight.setFlightNumber(flightNumber);
        flight.setDepartureTime(LocalDateTime.parse(departureTime, formatter));
        flight.setArrivalTime(LocalDateTime.parse(arrivalTime, formatter));
        flight.setReservationCutoffHours(reservationCutoffHours);
        flight.setCancellationCutoffHours(cancellationCutoffHours);
        flight.setAirplaneId(airplaneId);
        flight.setDepartureCityId(departureCityId);
        flight.setArrivalCityId(arrivalCityId);
        flight.setCreatedAt(LocalDateTime.now());
        
        flightRepository.saveFlight(flight);
    }
    
    public void updateFlight(String id, String flightNumber, String departureTime, String arrivalTime,
                             Integer reservationCutoffHours, Integer cancellationCutoffHours,
                             Integer airplaneId, Integer departureCityId, Integer arrivalCityId) {
        
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        
        Flight flight = flightRepository.getFlightById(Integer.parseInt(id));
        flight.setFlightNumber(flightNumber);
        flight.setDepartureTime(LocalDateTime.parse(departureTime, formatter));
        flight.setArrivalTime(LocalDateTime.parse(arrivalTime, formatter));
        flight.setReservationCutoffHours(reservationCutoffHours);
        flight.setCancellationCutoffHours(cancellationCutoffHours);
        flight.setAirplaneId(airplaneId);
        flight.setDepartureCityId(departureCityId);
        flight.setArrivalCityId(arrivalCityId);
        
        flightRepository.editFlight(flight);
    }
    
    public void deleteFlight(String id) {
        flightRepository.deleteFlight(Integer.parseInt(id));
    }
}
