package mg.itu.avion.controller;

import com.ETU1792.annotation.Authentified;
import com.ETU1792.annotation.Controller;
import com.ETU1792.annotation.GET;
import com.ETU1792.annotation.ParamObject;
import com.ETU1792.annotation.Role;
import com.ETU1792.utils.ModelView;
import com.ETU1792.utils.MySession;

import java.time.LocalDate;
import java.util.List;

import mg.itu.avion.airplane.ClassRepository;
import mg.itu.avion.airplane.ClassService;
import mg.itu.avion.city.CityRepository;
import mg.itu.avion.city.CityService;
import mg.itu.avion.flight.*;
import mg.itu.avion.flight.search.FlightSearchCriteria;
import mg.itu.avion.passenger.PassengerTypeRepository;
import mg.itu.avion.passenger.PassengerTypeService;
import mg.itu.avion.utils.MyBatisUtil;

@Controller
@Authentified
@Role({"USER", "ADMIN"})
public class FlightSearchCriteriaController {
    
    private final FlightService flightService;
    private final CityService cityService;
    private final ClassService classService;
    private final PassengerTypeService passengerTypeService;
    
    public FlightSearchCriteriaController() {
        this.flightService = new FlightService(new FlightRepository(MyBatisUtil.getSqlSessionFactory()));
        this.cityService = new CityService(new CityRepository(MyBatisUtil.getSqlSessionFactory()));
        this.classService = new ClassService(new ClassRepository(MyBatisUtil.getSqlSessionFactory()));
        this.passengerTypeService = new PassengerTypeService(new PassengerTypeRepository(MyBatisUtil.getSqlSessionFactory()));
    }

    @GET("flight/search")
    public ModelView searchFlights(@ParamObject FlightSearchCriteria criteria, MySession session) {
        ModelView mv = new ModelView("/layouts/sidebar.jsp");
        mv.addObject("contentJsp", "/views/flight/search.jsp");
        mv.addObject("activeMenu", "searchFlights");
        mv.addObject("pageTitle", "Recherche avancée de vols");

        // Listes pour les filtres
        mv.addObject("cities", cityService.getAllCities());
        mv.addObject("classes", classService.getAllClasses());
        mv.addObject("passengerTypes", passengerTypeService.getAllPassengerTypes());

        // Si aucun critère renseigné -> juste afficher le formulaire
        boolean hasAny =
            criteria != null && (
               criteria.getDepartureCityId() != null ||
               criteria.getArrivalCityId() != null ||
               criteria.getDepartureDateFrom() != null ||
               criteria.getDepartureDateTo() != null ||
               criteria.getClassId() != null ||
               criteria.getPassengerTypeId() != null ||
               criteria.getMinPrice() != null ||
               criteria.getMaxPrice() != null ||
               Boolean.TRUE.equals(criteria.getPromoOnly())
            );

        if (!hasAny) return mv;

        // Normaliser les bornes de date
        if (criteria.getDepartureDateFrom() != null) {
            LocalDate d = criteria.getDepartureDateFrom();
            criteria.setDepartureFromDateTime(d.atStartOfDay());
        }
        if (criteria.getDepartureDateTo() != null) {
            LocalDate d = criteria.getDepartureDateTo();
            // < toDate+1j (exclus)
            criteria.setDepartureToDateTime(d.plusDays(1).atStartOfDay());
        }

        // Chercher
        List<Flight> results = flightService.searchFlightsAdvanced(criteria);
        mv.addObject("results", results);
        mv.addObject("criteria", criteria);
        return mv;
    }
}