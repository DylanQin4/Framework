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
            new FlightService(new FlightRepository(MyBatisUtil.getSqlSessionFactory()))
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
        ModelView mv = new ModelView("/views/reservation/index.jsp");
        mv.addObject("reservations", reservations); // Liste des réservations

        return mv;
    }

    @GET("reservations/add")
    public ModelView getAddReservation() {
        List<Flight> flights = flightService.getAllFlights();
        List<Class> classes = classService.getAllClasses();

        // Préparer le modèle pour la vue
        ModelView mv = new ModelView("/views/reservation/add.jsp");
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
    
        // Sauvegarder le fichier de passeport
        if (filePathPassport != null && filePathPassport.getSize() > 0) {
            String filePath = saveFileToServer(filePathPassport);
            System.out.println("File path: " + filePath);
            reservation.setFilePathPassport(filePath);
        }
    
        // Enregistrer la réservation
        reservation.setUserId(userId);
    
        try {
            reservationService.createReservation(reservation);
    
            ModelView mv = new ModelView("reservations");
            mv.setIsRedirect(true);
            return mv;
        } catch (IllegalArgumentException e) {
            ModelView mv = new ModelView("reservations/add");
            mv.addObject("errorMessage", e.getMessage());
            return mv;
        }
    }

    // Méthode pour sauvegarder un fichier sur le serveur
    private String saveFileToServer(Part filePart) {
        try {
            String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
            System.out.println("File name: " + fileName);
            String uploadPath = "/var/itu/LohataonaFramework/uploads/" + fileName;
            System.out.println("Upload path: " + uploadPath);
            filePart.write(uploadPath);
            return uploadPath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
