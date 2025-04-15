package mg.itu.avion.airplane;

import java.util.List;

public class AirplaneService {
    private AirplaneRepository airplaneRepository;
    
    public AirplaneService(AirplaneRepository airplaneRepository) {
        this.airplaneRepository = airplaneRepository;
    }
    
    public List<Airplane> getAllAirplanes() {
        return airplaneRepository.getAllAirplanes();
    }
    
    public Airplane getAirplaneById(String id) {
        return airplaneRepository.getAirplaneById(Integer.parseInt(id));
    }

    public Integer getSeatCountByAirplaneIdClassId(int airplaneId, int classId) {
        return airplaneRepository.getSeatCountByAirplaneIdClassId(airplaneId, classId);
    }
}
