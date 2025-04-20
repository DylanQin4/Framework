package mg.itu.avion.controller;

import com.ETU1792.annotation.Controller;
import com.ETU1792.annotation.GET;
import com.ETU1792.annotation.Param;
import com.ETU1792.annotation.JSON;

import mg.itu.avion.reservation.ReservationService;
import mg.itu.avion.utils.MyBatisUtil;
import mg.itu.avion.airplane.AirplaneRepository;
import mg.itu.avion.airplane.AirplaneService;
import mg.itu.avion.config.ConfigurationRepository;
import mg.itu.avion.config.ConfigurationService;
import mg.itu.avion.flight.FlightClassPassengerRepository;
import mg.itu.avion.flight.FlightClassPassengerService;
import mg.itu.avion.flight.FlightRepository;
import mg.itu.avion.flight.FlightService;
import mg.itu.avion.mappers.ReservationDTO;
import mg.itu.avion.mappers.ReservationMapperDTO;
import mg.itu.avion.passenger.PassengerTypeRepository;
import mg.itu.avion.passenger.PassengerTypeService;
import mg.itu.avion.reservation.Reservation;
import mg.itu.avion.reservation.ReservationRepository;

@Controller
public class ApiController {
    private final ReservationService reservationService;

    public ApiController() {
        this.reservationService = new ReservationService(
            new ReservationRepository(MyBatisUtil.getSqlSessionFactory()),
            new FlightClassPassengerService(new FlightClassPassengerRepository(MyBatisUtil.getSqlSessionFactory())),
            new PassengerTypeService(new PassengerTypeRepository(MyBatisUtil.getSqlSessionFactory())),
            new AirplaneService(new AirplaneRepository(MyBatisUtil.getSqlSessionFactory())),
            new FlightService(new FlightRepository(MyBatisUtil.getSqlSessionFactory())),
            new ConfigurationService(new ConfigurationRepository(MyBatisUtil.getSqlSessionFactory()))
        );
    }

    @GET("api/reservation/{id}")
    @JSON
    public ReservationDTO getReservationById(@Param(name = "id") Integer id) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("Paramètre {id} manquant.");
        }

        Reservation reservation = reservationService.getReservationById(id);
        if (reservation == null) {
            throw new RuntimeException("Réservation introuvable pour id=" + id);
        }

        return ReservationMapperDTO.mapToDTO(reservation);
    }
}
