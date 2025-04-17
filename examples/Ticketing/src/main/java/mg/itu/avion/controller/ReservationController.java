package mg.itu.avion.controller;

import java.util.List;

import javax.servlet.http.Part;

import com.ETU1792.annotation.Authentified;
import com.ETU1792.annotation.Controller;
import com.ETU1792.annotation.FormView;
import com.ETU1792.annotation.GET;
import com.ETU1792.annotation.POST;
import com.ETU1792.annotation.Param;
import com.ETU1792.annotation.ParamObject;
import com.ETU1792.annotation.Role;
import com.ETU1792.utils.ModelView;
import com.ETU1792.utils.MySession;

import mg.itu.avion.flight.Flight;
import mg.itu.avion.flight.FlightClassPassengerRepository;
import mg.itu.avion.flight.FlightClassPassengerService;
import mg.itu.avion.flight.FlightRepository;
import mg.itu.avion.flight.FlightService;
import mg.itu.avion.passenger.PassengerTypeRepository;
import mg.itu.avion.passenger.PassengerTypeService;
import mg.itu.avion.reservation.Reservation;
import mg.itu.avion.reservation.ReservationRepository;
import mg.itu.avion.reservation.ReservationService;
import mg.itu.avion.utils.MyBatisUtil;
import mg.itu.avion.airplane.AirplaneRepository;
import mg.itu.avion.airplane.AirplaneService;
import mg.itu.avion.airplane.Class;
import mg.itu.avion.airplane.ClassRepository;
import mg.itu.avion.airplane.ClassService;
import mg.itu.avion.config.ConfigurationRepository;
import mg.itu.avion.config.ConfigurationService;

@Controller
@Authentified
@Role({"USER", "ADMIN"})
public class ReservationController {
    
    private final ReservationService reservationService;
    private final FlightService flightService;
    private final ClassService classService;

    public ReservationController() {
        this.reservationService = new ReservationService(
            new ReservationRepository(MyBatisUtil.getSqlSessionFactory()),
            new FlightClassPassengerService(new FlightClassPassengerRepository(MyBatisUtil.getSqlSessionFactory())),
            new PassengerTypeService(new PassengerTypeRepository(MyBatisUtil.getSqlSessionFactory())),
            new AirplaneService(new AirplaneRepository(MyBatisUtil.getSqlSessionFactory())),
            new FlightService(new FlightRepository(MyBatisUtil.getSqlSessionFactory())),
            new ConfigurationService(new ConfigurationRepository(MyBatisUtil.getSqlSessionFactory()))
        );
        this.flightService = new FlightService(new FlightRepository(MyBatisUtil.getSqlSessionFactory()));
        this.classService = new ClassService(new ClassRepository(MyBatisUtil.getSqlSessionFactory()));
    }

    @GET("reservations")
    public ModelView getAllReservations(MySession session) {
        Integer userId = (Integer) session.get("userId");
        if (session.get("userId") == null) {
            ModelView mv = new ModelView("login");
            mv.setIsRedirect(true);
            return mv;            
        }

        // Récupérer toutes les réservations
        List<Reservation> reservations = reservationService.getReservationsByUserId(userId);

        // Préparer le modèle pour la vue
        ModelView mv = new ModelView("/layouts/sidebar.jsp");
        mv.addObject("contentJsp", "/views/reservation/index.jsp");
        mv.addObject("activeMenu", "reservations");
        mv.addObject("pageTitle", "Mes Réservations");
        mv.addObject("reservations", reservations); // Liste des réservations

        return mv;
    }

    @GET("reservations/add")
    public ModelView getAddReservation() {
        List<Flight> flights = flightService.getAllFlights();
        List<Class> classes = classService.getAllClasses();

        // Préparer le modèle pour la vue
        ModelView mv = new ModelView("/layouts/sidebar.jsp");
        mv.addObject("contentJsp", "/views/reservation/add.jsp");
        mv.addObject("activeMenu", "reservations");
        mv.addObject("flights", flights); // Liste des vols
        mv.addObject("classes", classes); // Liste des classes

        return mv;
    }

    @POST("reservations/add")
    @FormView("reservations/add")
    public ModelView addReservation(@ParamObject Reservation reservation, @Param(name = "filePathPassport") Part filePathPassport, MySession session) {
        Integer userId = (Integer) session.get("userId");
        if (session.get("userId") == null) {
            ModelView mv = new ModelView("login");
            mv.setIsRedirect(true);
            return mv;            
        }

        ModelView mv = new ModelView("/layouts/sidebar.jsp");
        mv.addObject("contentJsp", "/views/reservation/add.jsp");
        mv.addObject("pageTitle", "Mes Réservations");
        mv.addObject("activeMenu", "reservations");

        // Sauvegarder le fichier de passeport
        String fileName = null;
        if (filePathPassport != null && filePathPassport.getSize() > 0) {
            fileName = System.currentTimeMillis() + "_" + filePathPassport.getSubmittedFileName();
            reservation.setFilePathPassport(fileName);
        } else {
            mv.addObject("errorMessage", "Please upload a passport file.");
            return mv;
        }
    
        // Enregistrer la réservation
        reservation.setUserId(userId);
    
        try {
            reservationService.createReservation(reservation);
            // save file to server
            saveFileToServer(filePathPassport, fileName);

            mv = new ModelView("reservations");
            mv.setIsRedirect(true);
            return mv;
        } catch (IllegalArgumentException e) {
            mv.addObject("errorMessage", e.getMessage());
            return mv;
        }
    }

    // Méthode pour sauvegarder un fichier sur le serveur
    private void saveFileToServer(Part filePart, String fileName) {
        try {
            System.out.println("File name: " + fileName);
            String uploadPath = "/var/itu/LohataonaFramework/uploads/" + fileName;
            System.out.println("Upload path: " + uploadPath);
            filePart.write(uploadPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GET("reservations/cancel")
    public ModelView cancelReservation(@Param(name = "reservationId") Integer reservationId, MySession session) {
        Integer userId = (Integer) session.get("userId");
        if (userId == null) {
            ModelView mv = new ModelView("login");
            mv.setIsRedirect(true);
            return mv;
        }

        try {
            Reservation reservation = reservationService.getReservationById(reservationId);
            if (reservation == null) {
                throw new IllegalArgumentException("Reservation not found.");
            }

            reservationService.cancelReservation(reservation);

            // Redirection vers la liste des réservations
            ModelView mv = new ModelView("reservations");
            mv.setIsRedirect(true);
            return mv;
        } catch (IllegalArgumentException e) {
            ModelView mv = new ModelView("/layouts/sidebar.jsp");
            mv.addObject("contentJsp", "/views/reservation/index.jsp");
            mv.addObject("activeMenu", "reservations");
            mv.addObject("errorMessage", e.getMessage());
            return mv;
        }
    }
}
