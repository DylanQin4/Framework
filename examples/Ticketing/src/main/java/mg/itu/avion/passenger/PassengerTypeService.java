package mg.itu.avion.passenger;

import java.util.List;

public class PassengerTypeService {
    private PassengerTypeRepository repository;

    public PassengerTypeService(PassengerTypeRepository repository) {
        this.repository = repository;
    }

    public List<PassengerType> getAllPassengerTypes() {
        return repository.getAllPassengerTypes();
    }

    public PassengerType getPassengerTypeById(Integer id) {
        return repository.getPassengerTypeById(id);
    }

    public PassengerType getPassengerTypeByAge(int age) {
        List<PassengerType> passengerTypes = repository.getAllPassengerTypes();
        for (PassengerType passengerType : passengerTypes) {
            if (passengerType.getStartAge() <= age && passengerType.getEndAge() >= age) {
                return passengerType;
            }
        }
        return null;
    }
}