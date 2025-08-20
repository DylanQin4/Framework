package mg.itu.avion.city;

import java.util.List;

public class CityService {
    private CityRepository cityRepository;
    
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }
    
    public List<City> getAllCities() {
        return cityRepository.getAllCities();
    }
    
    public City getCityById(String id) {
        return cityRepository.getCityById(Integer.parseInt(id));
    }
    
    public List<City> getCitiesByCountryId(String countryId) {
        return cityRepository.getCitiesByCountryId(Integer.parseInt(countryId));
    }
}
