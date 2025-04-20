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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mg.itu.avion.airplane.AirplaneRepository;
import mg.itu.avion.airplane.AirplaneService;
import mg.itu.avion.airplane.ClassRepository;
import mg.itu.avion.airplane.ClassService;
import mg.itu.avion.city.CityRepository;
import mg.itu.avion.city.CityService;
import mg.itu.avion.config.ConfigKey;
import mg.itu.avion.config.ConfigurationRepository;
import mg.itu.avion.config.ConfigurationService;
import mg.itu.avion.flight.*;
import mg.itu.avion.passenger.ConfigFares;
import mg.itu.avion.passenger.ConfigFaresRepository;
import mg.itu.avion.passenger.ConfigFaresService;
import mg.itu.avion.passenger.PassengerType;
import mg.itu.avion.passenger.PassengerTypeRepository;
import mg.itu.avion.passenger.PassengerTypeService;
import mg.itu.avion.utils.MyBatisUtil;

@Controller
@Authentified
@Role("ADMIN")
public class FlightController {
    
    private final FlightService flightService;
    private final AirplaneService airplaneService;
    private final CityService cityService;
    private final ConfigurationService configurationService;
    private final FlightClassPassengerService flightClassPassengerService;
    private final ClassService classService;
    private final PassengerTypeService passengerTypeService;
    private final ConfigFaresService configFaresService;
    
    public FlightController() {
        this.flightService = new FlightService(new FlightRepository(MyBatisUtil.getSqlSessionFactory()));
        this.airplaneService = new AirplaneService(new AirplaneRepository(MyBatisUtil.getSqlSessionFactory()));
        this.cityService = new CityService(new CityRepository(MyBatisUtil.getSqlSessionFactory()));
        this.configurationService = new ConfigurationService(new ConfigurationRepository(MyBatisUtil.getSqlSessionFactory()));
        this.flightClassPassengerService = new FlightClassPassengerService(new FlightClassPassengerRepository(MyBatisUtil.getSqlSessionFactory()));
        this.classService = new ClassService(new ClassRepository(MyBatisUtil.getSqlSessionFactory()));
        this.passengerTypeService = new PassengerTypeService(new PassengerTypeRepository(MyBatisUtil.getSqlSessionFactory()));
        this.configFaresService = new ConfigFaresService(new ConfigFaresRepository(MyBatisUtil.getSqlSessionFactory()));
    }

    @GET("admin/flights")
    public ModelView getFlights() {
        List<Flight> flights = new FlightService(new FlightRepository(MyBatisUtil.getSqlSessionFactory())).getAllFlights();
        ModelView mv = new ModelView("/layouts/sidebar.jsp");
        mv.addObject("contentJsp", "/admin/flights/list.jsp");
        mv.addObject("pageTitle", "Liste des vols");
        mv.addObject("activeMenu", "flights");
        mv.addObject("flights", flights);
        return mv;
    }
    
    @GET("admin/flights/add")
    public ModelView getAddFlight() {
        ModelView mv = new ModelView("/layouts/sidebar.jsp");
        mv.addObject("contentJsp", "/admin/flights/add.jsp");
        mv.addObject("pageTitle", "Nouveau vol");
        mv.addObject("activeMenu", "flights");
        mv.addObject("airplanes", airplaneService.getAllAirplanes());
        mv.addObject("cities", cityService.getAllCities());
        mv.addObject("classes", classService.getAllClasses());
        mv.addObject("passengerTypes", passengerTypeService.getAllPassengerTypes());
        mv.addObject("reservation_cutoff_hours", configurationService.getConfigurationByKey(ConfigKey.RESERVATION_CUTOFF_HOURS.getDatabaseKey()).getConfigValue());
        mv.addObject("cancellation_cutoff_hours", configurationService.getConfigurationByKey(ConfigKey.CANCELLATION_CUTOFF_HOURS.getDatabaseKey()).getConfigValue());
        mv.addObject("promotion_limit", configurationService.getConfigurationByKey(ConfigKey.PROMOTION_LIMIT.getDatabaseKey()).getConfigValue());
        mv.addObject("promotion_discount", configurationService.getConfigurationByKey(ConfigKey.PROMOTION_DISCOUNT.getDatabaseKey()).getConfigValue());
        
        // Récupérer les tarifs par défaut pour chaque type de passager
        Map<Integer, Double> defaultPrices = new HashMap<>();
        for (PassengerType passengerType : passengerTypeService.getAllPassengerTypes()) {
            ConfigFares latestFares = configFaresService.getLatestConfigFaresByPassengerType(passengerType.getId());
            defaultPrices.put(passengerType.getId(), latestFares != null ? latestFares.getPrice() : 0.0);
        }
        mv.addObject("defaultPrices", defaultPrices);


        // Calcul des dates de départ et d'arrivée par défaut
        LocalDateTime now = LocalDateTime.now();
        mv.addObject("defaultDepartureTime", now.withHour(8).withMinute(0));
        mv.addObject("defaultArrivalTime", now.plusDays(1).withHour(10).withMinute(0));

        return mv;
    }

    
    @POST("admin/flights/add")
    @FormView("admin/flights/add")
    public ModelView addFlight(@ParamObject Flight newFlight, @Param(name = "flightClassPassengerData") String flightClassPassengerData) {
        // Enregistrer le vol
        newFlight.setCreatedAt(LocalDateTime.now());
        flightService.createFlight(newFlight);

        // Découper la chaîne pour extraire les données
        if (flightClassPassengerData != null && !flightClassPassengerData.isEmpty()) {
            String[] entries = flightClassPassengerData.split("\\|"); // Séparer chaque entrée par "|"
    
            for (String entry : entries) {
                String[] fields = entry.split(","); // Séparer chaque champ par ","
                if (fields.length == 5) {
                    Integer classId = Integer.parseInt(fields[0]);
                    Integer passengerTypeId = Integer.parseInt(fields[1]);
                    Double basePrice = Double.parseDouble(fields[2]);
                    Integer promotionLimit = Integer.parseInt(fields[3]);
                    Double promotionDiscount = Double.parseDouble(fields[4]);
    
                    // Créer un objet FlightClassPassenger
                    FlightClassPassenger fcp = new FlightClassPassenger();
                    fcp.setFlightId(newFlight.getId());
                    fcp.setClassId(classId);
                    fcp.setPassengerTypeId(passengerTypeId);
                    fcp.setBasePrice(basePrice);
                    fcp.setPromotionLimit(promotionLimit);
                    fcp.setPromotionDiscount(promotionDiscount);

                    // Enregistrer dans la base de données
                    flightClassPassengerService.addFlightClassPassenger(fcp);
                }
            }
        }
    
        // Redirection vers la liste des vols
        ModelView mv = new ModelView("admin/flights");
        mv.setIsRedirect(true);
        return mv;
    }

    @GET("admin/flights/edit")
    public ModelView getEditFlight(@Param(name = "id") String id) {
        // Récupérer le vol à éditer
        Flight flight = flightService.getFlightById(Integer.parseInt(id));
        
        // Récupérer les tarifs et promotions pour ce vol
        List<FlightClassPassenger> flightClassPassengers = flightClassPassengerService.getFlightClassPassengersByFlightId(flight.getId());
    
        // Préparer le modèle pour la vue
        ModelView mv = new ModelView("/layouts/sidebar.jsp");
        mv.addObject("contentJsp", "/admin/flights/edit.jsp");
        mv.addObject("pageTitle", "Modification du vol " + flight.getFlightNumber());
        mv.addObject("activeMenu", "flights");
        mv.addObject("flight", flight);
        mv.addObject("airplanes", airplaneService.getAllAirplanes());
        mv.addObject("cities", cityService.getAllCities());
        mv.addObject("classes", classService.getAllClasses());
        mv.addObject("passengerTypes", passengerTypeService.getAllPassengerTypes());
        mv.addObject("flightClassPassengers", flightClassPassengers);

        System.out.println("flightClassPassengers : " + flightClassPassengers);
    
        return mv;
    }
    
    @POST("admin/flights/edit")
    public ModelView editFlight(@ParamObject Flight flight) {
        // Mettre à jour les informations du vol
        flightService.updateFlight(flight);
    
        // Redirection vers la liste des vols
        ModelView mv = new ModelView("admin/flights");
        mv.setIsRedirect(true);
        return mv;
    }

    @GET("admin/flights/delete")
    public ModelView deleteFlight(@Param(name = "id") String id) {
        flightService.deleteFlight(id);
        ModelView mv = new ModelView("admin/flights");
        mv.setIsRedirect(true);
        return mv;
    }
}