package mg.itu.avion.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import lombok.AllArgsConstructor;
import mg.itu.avion.flight.FlightClassPassenger;
import mg.itu.avion.flight.FlightClassPassengerService;
import mg.itu.avion.passenger.PassengerType;
import mg.itu.avion.passenger.PassengerTypeService;

@AllArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final FlightClassPassengerService flightClassPassengerService;
    private final PassengerTypeService passengerTypeService;

    public List<Reservation> getAllReservations() {
        return reservationRepository.getAllReservations();
    }

    public void createReservation(Reservation reservation) {
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());
        reservation.setStatus(ReservationStatus.RESERVED); // Default status

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
        System.out.println("Flight ID: " + reservation.getFlightId());
        System.out.println("Class ID: " + reservation.getClassId());
        System.out.println("Passenger Type ID: " + passengerTypeId);
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

    public List<Reservation> getReservationsByUserId(Integer userId) {
        List<Reservation> reservations = reservationRepository.getAllReservations();
        return reservations.stream()
                .filter(reservation -> reservation.getUserId().equals(userId))
                .toList();
    }
}