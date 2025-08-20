package mg.itu.avion.passenger;

public class ConfigFaresService {
    private ConfigFaresRepository repository;

    public ConfigFaresService(ConfigFaresRepository repository) {
        this.repository = repository;
    }

    public ConfigFares getLatestConfigFares() {
        return repository.getLatestConfigFares();
    }

    public ConfigFares getLatestConfigFaresByPassengerType(Integer passengerTypeId) {
        return repository.getLatestConfigFaresByPassengerType(passengerTypeId);
    }
}