package mg.itu.avion.flight;

import java.util.List;

public class FlightClassPassengerService {
    private FlightClassPassengerRepository repository;

    public FlightClassPassengerService(FlightClassPassengerRepository repository) {
        this.repository = repository;
    }

    public void addFlightClassPassenger(FlightClassPassenger flightClassPassenger) {
        repository.saveFlightClassPassenger(flightClassPassenger);
    }

    public List<FlightClassPassenger> getFlightClassPassengersByFlightId(Integer flightId) {
        return repository.getFlightClassPassengersByFlightId(flightId);
    }

    public void deleteFlightClassPassengersByFlightId(Integer id) {
        repository.deleteFlightClassPassengersByFlightId(id);
    }
}