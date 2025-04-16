package mg.itu.avion.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import lombok.AllArgsConstructor;
import mg.itu.avion.airplane.AirplaneService;
import mg.itu.avion.config.ConfigKey;
import mg.itu.avion.config.ConfigurationService;
import mg.itu.avion.flight.FlightClassPassenger;
import mg.itu.avion.flight.FlightClassPassengerService;
import mg.itu.avion.flight.FlightService;
import mg.itu.avion.passenger.PassengerType;
import mg.itu.avion.passenger.PassengerTypeService;

@AllArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final FlightClassPassengerService flightClassPassengerService;
    private final PassengerTypeService passengerTypeService;
    private final AirplaneService airplaneService;
    private final FlightService flightService;
    private final ConfigurationService configurationService;

    public List<Reservation> getAllReservations() {
        return reservationRepository.getAllReservations();
    }

    public Reservation getReservationById(Integer id) {
        return reservationRepository.getReservationById(id);
    }

    public void cancelReservation(Reservation reservation) {
        LocalDateTime currentTime = LocalDateTime.now();
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setUpdatedAt(currentTime);
        reservation.setCancellationDate(currentTime);

        // verify if the annulation is not possible
        String cancellationCutoffHours = configurationService.getConfigurationByKey(ConfigKey.CANCELLATION_CUTOFF_HOURS.getDatabaseKey()).getConfigValue();
        int cutoffHours = Integer.parseInt(cancellationCutoffHours);
        LocalDateTime departureTime = flightService.getFlightById(reservation.getFlightId()).getDepartureTime();
        if (departureTime.minusHours(cutoffHours).isBefore(currentTime)) {
            throw new IllegalArgumentException("Reservation cannot be cancelled within " + cutoffHours + " hours of departure.");
        }
        reservationRepository.updateReservation(reservation);
    }

    public void createReservation(Reservation reservation) {
        LocalDateTime currentTime = LocalDateTime.now();
        reservation.setCreatedAt(currentTime);
        reservation.setUpdatedAt(currentTime);
        reservation.setStatus(ReservationStatus.RESERVED); // Default status

        String reservationCutoffHours = configurationService.getConfigurationByKey(ConfigKey.RESERVATION_CUTOFF_HOURS.getDatabaseKey()).getConfigValue();
        int cutoffHours = Integer.parseInt(reservationCutoffHours);
        LocalDateTime departureTime = flightService.getFlightById(reservation.getFlightId()).getDepartureTime();
        if (departureTime.minusHours(cutoffHours).isBefore(currentTime)) {
            System.out.println("Reservation cannot be made within " + cutoffHours + " hours of departure.");
            throw new IllegalArgumentException("Reservation cannot be made within " + cutoffHours + " hours of departure.");
        }

        // get seat disponibility by classId
        int seatDispo = (int)airplaneService.getSeatCountByAirplaneIdClassId(flightService.getFlightById(reservation.getFlightId()).getAirplaneId(), reservation.getClassId());
        int seatReserved = countReservationsByFlightIdClassId(reservation.getFlightId(), reservation.getClassId());
        System.out.println("Seat available: " + seatDispo);
        System.out.println("Seat reserved: " + seatReserved);
        // check if there is available seat
        if (seatDispo <= seatReserved) {
            throw new IllegalArgumentException("No available seats in this class.");
        }

        // get age of the passenger
        LocalDate birthDate = reservation.getPassengerBirthdate();
        LocalDate today = LocalDate.now();
        int age = Period.between(birthDate, today).getYears();
        PassengerType passengerType = passengerTypeService.getPassengerTypeByAge(age);
        if (passengerType != null) {
            reservation.setClassId(passengerType.getId());
        } else {
            throw new IllegalArgumentException("Invalid passenger type for age: " + age);
        }
        // Set the passenger type based on age
        Integer passengerTypeId = passengerTypeService.getPassengerTypeByAge(age).getId();
        FlightClassPassenger flightClassPassenger = flightClassPassengerService.getFlightClassPassengerById(reservation.getFlightId(), reservation.getClassId(), passengerTypeId);

        boolean isPromotionAvailable = false;
        int nbReservations = countReservationsByFlightId(reservation.getFlightId());
        int maxReservations = flightClassPassenger.getPromotionLimit();
        if (nbReservations < maxReservations) {
            isPromotionAvailable = true;
        }

        if (isPromotionAvailable) {
            reservation.setDiscount(flightClassPassenger.getPromotionDiscount());
        } else {
            reservation.setDiscount(0.0);
        }

        // Set the total amount based on the flight and class
        Double flightPrice = flightClassPassenger.getBasePrice();

        reservation.setAmount(flightPrice);

        reservationRepository.saveReservation(reservation);
    }

    public int countReservationsByFlightId(Integer flightId) {
        List<Reservation> reservations = reservationRepository.getAllReservations();
        return (int) reservations.stream()
                .filter(reservation -> reservation.getFlightId().equals(flightId))
                .count();
    }

    public int countReservationsByFlightIdClassId(Integer flightId, Integer classId) {
        List<Reservation> reservations = reservationRepository.getAllReservations();
        return (int) reservations.stream()
                .filter(reservation -> reservation.getFlightId().equals(flightId) && reservation.getClassId().equals(classId))
                .count();
    }

    public List<Reservation> getReservationsByUserId(Integer userId) {
        List<Reservation> reservations = reservationRepository.getAllReservations();
        return reservations.stream()
                .filter(reservation -> reservation.getUserId().equals(userId))
                .toList();
    }
}