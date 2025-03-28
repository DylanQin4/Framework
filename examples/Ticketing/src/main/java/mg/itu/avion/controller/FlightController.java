package mg.itu.avion.controller;

import com.ETU1792.annotation.Authentified;
import com.ETU1792.annotation.Controller;
import com.ETU1792.annotation.FormView;
import com.ETU1792.annotation.GET;
import com.ETU1792.annotation.POST;
import com.ETU1792.annotation.Param;
import com.ETU1792.annotation.ParamObject;
import com.ETU1792.annotation.Role;
import com.ETU1792.utils.ModelView;

import java.time.LocalDateTime;
import java.util.List;

import mg.itu.avion.airplane.AirplaneRepository;
import mg.itu.avion.airplane.AirplaneService;
import mg.itu.avion.city.CityRepository;
import mg.itu.avion.city.CityService;
import mg.itu.avion.config.ConfigKey;
import mg.itu.avion.config.ConfigurationRepository;
import mg.itu.avion.config.ConfigurationService;
import mg.itu.avion.flight.*;
import mg.itu.avion.utils.MyBatisUtil;

@Controller
@Authentified
@Role("ADMIN")
public class FlightController {
    
    private FlightService flightService;
    private AirplaneService airplaneService;
    private CityService cityService;
    private ConfigurationService configurationService;
    
    public FlightController() {
        this.flightService = new FlightService(new FlightRepository(MyBatisUtil.getSqlSessionFactory()));
        this.airplaneService = new AirplaneService(new AirplaneRepository(MyBatisUtil.getSqlSessionFactory()));
        this.cityService = new CityService(new CityRepository(MyBatisUtil.getSqlSessionFactory()));
        this.configurationService = new ConfigurationService(new ConfigurationRepository(MyBatisUtil.getSqlSessionFactory()));
    }
    
    @GET("flights")
    public ModelView getFlights() {
        List<Flight> flights = new FlightService(new FlightRepository(MyBatisUtil.getSqlSessionFactory())).getAllFlights();
        ModelView mv = new ModelView("/admin/flights/index.jsp");
        mv.addObject("flights", flights);
        return mv;
    }
    
    @GET("flights/add")
    public ModelView getAddFlight() {
        ModelView mv = new ModelView("/admin/flights/add.jsp");
        mv.addObject("airplanes", airplaneService.getAllAirplanes());
        mv.addObject("cities", cityService.getAllCities());
        mv.addObject("reservation_cutoff_hours", configurationService.getConfigurationByKey(ConfigKey.RESERVATION_CUTOFF_HOURS.getDatabaseKey()).getConfigValue());
        mv.addObject("cancellation_cutoff_hours", configurationService.getConfigurationByKey(ConfigKey.CANCELLATION_CUTOFF_HOURS.getDatabaseKey()).getConfigValue());
        return mv;
    }
    
    @POST("flights/add")
    @FormView("flights/add")
    public ModelView addFlight(@ParamObject Flight newFlight) {
        
        newFlight.setCreatedAt(LocalDateTime.now());
        flightService.createFlight(newFlight);
        
        ModelView mv = new ModelView("flights");
        mv.setIsRedirect(true);
        return mv;
    }
    
    @GET("flights/edit")
    public ModelView getEditFlight(@Param(name = "id") String id) {
        Flight flight = flightService.getFlightById(id);
        
        ModelView mv = new ModelView("/admin/flights/edit.jsp");
        mv.addObject("flight", flight);
        mv.addObject("airplanes", airplaneService.getAllAirplanes());
        mv.addObject("cities", cityService.getAllCities());
        return mv;
    }
    
    @POST("flights/edit")
    public ModelView editFlight(@ParamObject Flight flight) {
            
        flightService.updateFlight(flight);
        
        ModelView mv = new ModelView("flights");
        mv.setIsRedirect(true);
        return mv;
    }
    
    @GET("flights/delete")
    public ModelView deleteFlight(@Param(name = "id") String id) {
        flightService.deleteFlight(id);
        ModelView mv = new ModelView("flights");
        mv.setIsRedirect(true);
        return mv;
    }
}