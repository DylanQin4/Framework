package mg.itu.avion.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Part;

import com.ETU1792.annotation.Authentified;
import com.ETU1792.annotation.Controller;
import com.ETU1792.annotation.FormView;
import com.ETU1792.annotation.GET;
import com.ETU1792.annotation.POST;
import com.ETU1792.annotation.Param;
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
import mg.itu.avion.reservation.ReservationPassenger;
import mg.itu.avion.reservation.ReservationRepository;
import mg.itu.avion.reservation.ReservationService;
import mg.itu.avion.reservation.ReservationStatus;
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
        if (userId == null) {
            ModelView mv = new ModelView("login");
            mv.setIsRedirect(true);
            return mv;
        }

        List<Reservation> reservations = reservationService.getReservationsByUserId(userId);

        ModelView mv = new ModelView("/layouts/sidebar.jsp");
        mv.addObject("contentJsp", "/views/reservation/index.jsp");
        mv.addObject("activeMenu", "reservations");
        mv.addObject("pageTitle", "Mes Réservations");
        mv.addObject("reservations", reservations);
        return mv;
    }

    @GET("reservations/add")
    public ModelView getAddReservation() {
        List<Flight> flights = flightService.getAllFlights();
        List<Class> classes = classService.getAllClasses();

        ModelView mv = new ModelView("/layouts/sidebar.jsp");
        mv.addObject("contentJsp", "/views/reservation/add.jsp");
        mv.addObject("activeMenu", "reservations");
        mv.addObject("pageTitle", "Ajouter une Réservation");
        mv.addObject("flights", flights);
        mv.addObject("classes", classes);
        return mv;
    }

    @POST("reservations/add")
    @FormView("reservations/add")
    public ModelView addReservation(
        @Param(name = "flightId") Integer flightId,
        @Param(name = "passengerName") String[] passengerNames,
        @Param(name = "passengerBirthdate") String[] passengerBirthdates, // yyyy-MM-dd
        @Param(name = "classId") Integer[] classIds,
        @Param(name = "filePathPassport") Part[] filePathPassports,       // optionnel, 1 fichier par passager
        MySession session
    ) {
        Integer userId = (Integer) session.get("userId");
        if (userId == null) {
            ModelView mv = new ModelView("login");
            mv.setIsRedirect(true);
            return mv;
        }

        System.out.println("Adding reservation for userId: " + userId);
        System.out.println("Flight ID: " + flightId);
        System.out.println("Passenger Names: " + (passengerNames != null ? String.join(", ", passengerNames) : "null"));
        System.out.println("Passenger Birthdates: " + (passengerBirthdates != null ? String.join(", ", passengerBirthdates) : "null"));
        System.out.println("Class IDs: " + (classIds != null ? String.join(", ", Arrays.stream(classIds).map(String::valueOf).toArray(String[]::new)) : "null"));
        System.out.println("File Path Passports: " + (filePathPassports != null ? Arrays.stream(filePathPassports).map(Part::getSubmittedFileName).collect(Collectors.joining(", ")) : "null"));

        ModelView mv = new ModelView("/layouts/sidebar.jsp");
        mv.addObject("contentJsp", "/views/reservation/add.jsp");
        mv.addObject("activeMenu", "reservations");
        mv.addObject("pageTitle", "Ajouter une Réservation");
        mv.addObject("flights", flightService.getAllFlights());
        mv.addObject("classes", classService.getAllClasses());

        try {
            if (flightId == null) throw new IllegalArgumentException("Vol non renseigné.");
            if (passengerNames == null || passengerBirthdates == null || classIds == null)
                throw new IllegalArgumentException("Champs passagers manquants.");
            int n = passengerNames.length;
            if (passengerBirthdates.length != n || classIds.length != n)
                throw new IllegalArgumentException("Les champs passagers n'ont pas la même longueur.");

            // Entête
            Reservation header = new Reservation();
            header.setUserId(userId);
            header.setFlightId(flightId);
            header.setStatus(ReservationStatus.RESERVED);
            header.setCreatedAt(LocalDateTime.now());
            header.setUpdatedAt(LocalDateTime.now());
            header.setTotalAmount(0.0);
            header.setTotalDiscount(0.0);

            // Détails
            List<ReservationPassenger> passengers = new ArrayList<>();
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (int i = 0; i < n; i++) {
                String name = (passengerNames[i] != null) ? passengerNames[i].trim() : null;
                String birth = (passengerBirthdates[i] != null) ? passengerBirthdates[i].trim() : null;
                Integer cls = classIds[i];

                if (name == null || name.isEmpty() || birth == null || birth.isEmpty() || cls == null) {
                    throw new IllegalArgumentException("Données passager incomplètes (index " + i + ").");
                }

                ReservationPassenger p = new ReservationPassenger();
                p.setPassengerName(name);
                p.setPassengerBirthdate(LocalDate.parse(birth, df));
                p.setClassId(cls);
                p.setCreatedAt(LocalDateTime.now());

                // Fichier passeport (optionnel)
                if (filePathPassports != null && i < filePathPassports.length) {
                    Part part = filePathPassports[i];
                    if (part != null && part.getSize() > 0 &&
                        part.getSubmittedFileName() != null && !part.getSubmittedFileName().isEmpty()) {
                        String fileName = saveFileToServer(part, "/var/itu/LohataonaFramework/uploads");
                        p.setFilePathPassport(fileName);
                    }
                }

                passengers.add(p);
            }

            // Persist: calcule types/prix/promo/total et insère entête + lignes
            reservationService.createReservation(header, passengers);

            mv = new ModelView("reservations");
            mv.setIsRedirect(true);
            return mv;

        } catch (IllegalArgumentException ex) {
            mv.addObject("errorMessage", ex.getMessage());
            return mv;
        } catch (Exception ex) {
            ex.printStackTrace();
            mv.addObject("errorMessage", "Erreur lors de la création de la réservation.");
            return mv;
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
            if (reservation == null) throw new IllegalArgumentException("Reservation not found.");
            reservationService.cancelReservation(reservation);

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

    // ====== UPLOAD ======
    private static final java.util.Map<String, String> EXT_BY_MIME = new java.util.HashMap<>() {{
        put("image/png", ".png");
        put("image/jpeg", ".jpg"); // ou .jpeg
        put("application/pdf", ".pdf");
    }};

    private String saveFileToServer(javax.servlet.http.Part part, String uploadDir) throws Exception {
        if (part == null || part.getSize() == 0) return null;

        // 1) Nom original "propre" (évite les chemins complets Windows)
        String original = java.nio.file.Paths.get(part.getSubmittedFileName()).getFileName().toString();

        // 2) Extraire l'extension à partir du nom
        String ext = "";
        int dot = (original != null) ? original.lastIndexOf('.') : -1;
        if (dot >= 0 && dot < original.length() - 1) {
            ext = original.substring(dot).toLowerCase();   // ex: ".png"
        }

        // 3) Si pas d’extension, tenter via le Content-Type
        if (ext.isEmpty()) {
            String ct = part.getContentType();
            if (ct != null) {
                String guessed = EXT_BY_MIME.get(ct.toLowerCase());
                if (guessed != null) ext = guessed;
            }
        }

        // (Optionnel) whitelisting des extensions
        if (!ext.matches("\\.(png|jpg|jpeg|pdf)")) {
            throw new IllegalArgumentException("Extension de fichier non autorisée: " + ext);
        }

        // 4) Générer un nom unique + extension
        String fileName = System.currentTimeMillis() + "_" + java.util.UUID.randomUUID() + ext;

        // 5) Créer le dossier si besoin et écrire le fichier
        java.nio.file.Path dir = java.nio.file.Paths.get(uploadDir);
        java.nio.file.Files.createDirectories(dir);
        java.nio.file.Path dest = dir.resolve(fileName);
        try (java.io.InputStream in = part.getInputStream()) {
            java.nio.file.Files.copy(in, dest, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }

        // 6) Retourner le nom (à stocker en DB). Pour une URL publique, préfixe ton chemin /uploads/...
        return fileName;
    }


    @GET("reservations/edit")
    public ModelView getEditReservation(@Param(name = "id") Integer reservationId, MySession session) {
        Integer userId = (Integer) session.get("userId");
        if (userId == null) {
            ModelView mv = new ModelView("login");
            mv.setIsRedirect(true);
            return mv;
        }

        Reservation r = reservationService.getReservationById(reservationId);
        if (r == null) {
            ModelView mv = new ModelView("reservations");
            mv.setIsRedirect(true);
            return mv;
        }

        ModelView mv = new ModelView("/layouts/sidebar.jsp");
        mv.addObject("contentJsp", "/views/reservation/edit.jsp");
        mv.addObject("activeMenu", "reservations");
        mv.addObject("pageTitle", "Modifier la réservation #" + r.getId());

        mv.addObject("reservation", r);
        mv.addObject("flights", flightService.getAllFlights());
        mv.addObject("classes", classService.getAllClasses());
        return mv;
    }

    @POST("reservations/edit")
    @FormView("reservations/edit")
    public ModelView postEditReservation(
            @Param(name = "id") Integer reservationId,
            @Param(name = "flightId") Integer flightId,
            @Param(name = "passengerId") Integer[] passengerIds,                 // optionnel (lignes existantes)
            @Param(name = "passengerName") String[] passengerNames,
            @Param(name = "passengerBirthdate") String[] passengerBirthdates,     // yyyy-MM-dd
            @Param(name = "classId") Integer[] classIds,
            @Param(name = "existingFilePathPassport") String[] existingFiles,     // chemin déjà stocké (peut être null)
            @Param(name = "filePathPassport") Part[] fileParts,                   // fichiers re-upload (optionnels)
            MySession session) {

        Integer userId = (Integer) session.get("userId");
        if (userId == null) {
            ModelView mv = new ModelView("login");
            mv.setIsRedirect(true);
            return mv;
        }

        // Prépare MV de retour en cas d'erreur
        ModelView mv = new ModelView("/layouts/sidebar.jsp");
        mv.addObject("contentJsp", "/views/reservation/edit.jsp");
        mv.addObject("activeMenu", "reservations");
        mv.addObject("pageTitle", "Modifier la réservation #" + reservationId);
        mv.addObject("flights", flightService.getAllFlights());
        mv.addObject("classes", classService.getAllClasses());
        mv.addObject("reservation", reservationService.getReservationById(reservationId));

        try {
            if (reservationId == null) throw new IllegalArgumentException("Réservation non renseignée.");
            if (flightId == null)      throw new IllegalArgumentException("Vol non renseigné.");
            if (passengerNames == null || passengerBirthdates == null || classIds == null)
                throw new IllegalArgumentException("Champs passagers manquants.");

            int n = passengerNames.length;
            if (passengerBirthdates.length != n || classIds.length != n)
                throw new IllegalArgumentException("Les champs passagers n'ont pas la même longueur.");

            // Entête
            Reservation header = new Reservation();
            header.setId(reservationId);
            header.setUserId(userId); // garde le propriétaire
            header.setFlightId(flightId);
            header.setUpdatedAt(java.time.LocalDateTime.now());
            header.setStatus(ReservationStatus.RESERVED); // ou garder le status courant si tu préfères

            // Détails
            java.time.format.DateTimeFormatter df = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
            java.util.List<ReservationPassenger> passengers = new java.util.ArrayList<>();

            for (int i = 0; i < n; i++) {
                String name = passengerNames[i] != null ? passengerNames[i].trim() : null;
                String birth = passengerBirthdates[i] != null ? passengerBirthdates[i].trim() : null;
                Integer cls = classIds[i];

                if (name == null || name.isEmpty() || birth == null || birth.isEmpty() || cls == null) {
                    throw new IllegalArgumentException("Données passager incomplètes (index " + i + ").");
                }

                ReservationPassenger p = new ReservationPassenger();
                // id ligne si existante
                if (passengerIds != null && i < passengerIds.length) p.setId(passengerIds[i]);
                p.setPassengerName(name);
                p.setPassengerBirthdate(java.time.LocalDate.parse(birth, df));
                p.setClassId(cls);

                // Fichier : si nouveau fichier, écrase; sinon conserve l'existant transmis
                String keep = (existingFiles != null && i < existingFiles.length) ? existingFiles[i] : null;
                if (fileParts != null && i < fileParts.length) {
                    Part part = fileParts[i];
                    if (part != null) {
                        String fileName = saveFileToServer(part, "/var/itu/LohataonaFramework/uploads");
                        p.setFilePathPassport(fileName);
                    } else {
                        p.setFilePathPassport(keep);
                    }
                } else {
                    p.setFilePathPassport(keep);
                }
                passengers.add(p);
            }

            // Mise à jour (remplace toutes les lignes : delete + insert)
            reservationService.updateReservationWithPassengers(header, passengers);

            mv = new ModelView("detail?id=" + reservationId);
            mv.setIsRedirect(true);
            return mv;

        } catch (IllegalArgumentException ex) {
            mv.addObject("errorMessage", ex.getMessage());
            return mv;
        } catch (Exception ex) {
            ex.printStackTrace();
            mv.addObject("errorMessage", "Erreur lors de la mise à jour de la réservation.");
            return mv;
        }
    }


    @GET("reservations/detail")
    public ModelView getReservationDetail(@Param(name = "id") Integer reservationId, MySession session) {
        Integer userId = (Integer) session.get("userId");
        if (userId == null) {
            ModelView mv = new ModelView("login");
            mv.setIsRedirect(true);
            return mv;
        }

        Reservation r = reservationService.getReservationById(reservationId);
        if (r == null) {
            ModelView mv = new ModelView("reservations");
            mv.setIsRedirect(true);
            return mv;
        }

        var flight = flightService.getFlightById(r.getFlightId());
        var classes = classService.getAllClasses();

        // Map id->label pour la classe
        java.util.Map<Integer, String> classById = new java.util.HashMap<>();
        for (var c : classes) classById.put(c.getId(), c.getLabel());

        // Map id->typeName pour le type de passager (si dispo)
        var pts = new mg.itu.avion.passenger.PassengerTypeService(
            new mg.itu.avion.passenger.PassengerTypeRepository(mg.itu.avion.utils.MyBatisUtil.getSqlSessionFactory())
        ).getAllPassengerTypes();
        java.util.Map<Integer, String> passengerTypeById = new java.util.HashMap<>();
        if (pts != null) for (var pt : pts) passengerTypeById.put(pt.getId(), pt.getTypeName());

        ModelView mv = new ModelView("/layouts/sidebar.jsp");
        mv.addObject("contentJsp", "/views/reservation/detail.jsp");
        mv.addObject("activeMenu", "reservations");
        mv.addObject("pageTitle", "Détail réservation #" + r.getId());

        mv.addObject("reservation", r);
        mv.addObject("flight", flight);
        mv.addObject("classById", classById);
        mv.addObject("passengerTypeById", passengerTypeById);

        return mv;
    }

}
