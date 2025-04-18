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
        LocalDateTime now = LocalDateTime.now();
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setUpdatedAt(now);
        reservation.setCancelledAt(now);

        int cutoffHours = Integer.parseInt(
            configurationService.getConfigurationByKey(ConfigKey.CANCELLATION_CUTOFF_HOURS.getDatabaseKey())
                                .getConfigValue()
        );
        LocalDateTime departureTime = flightService.getFlightById(reservation.getFlightId()).getDepartureTime();
        if (departureTime.minusHours(cutoffHours).isBefore(now)) {
            throw new IllegalArgumentException("Reservation cannot be cancelled within " + cutoffHours + " hours of departure.");
        }
        reservationRepository.updateReservation(reservation);
    }

    /** Création d'une réservation avec N passagers */
    public void createReservation(Reservation header, List<ReservationPassenger> passengers) {
        LocalDateTime now = LocalDateTime.now();
        header.setCreatedAt(now);
        header.setUpdatedAt(now);
        header.setStatus(ReservationStatus.RESERVED);

        int cutoffHours = Integer.parseInt(
            configurationService.getConfigurationByKey(ConfigKey.RESERVATION_CUTOFF_HOURS.getDatabaseKey())
                                .getConfigValue()
        );
        LocalDateTime departureTime = flightService.getFlightById(header.getFlightId()).getDepartureTime();
        if (departureTime.minusHours(cutoffHours).isBefore(now)) {
            throw new IllegalArgumentException("Reservation cannot be made within " + cutoffHours + " hours of departure.");
        }

        double totalAmount = 0.0;
        double totalDiscount = 0.0;

        for (ReservationPassenger p : passengers) {
            // 1) Type d'âge
            int age = Period.between(p.getPassengerBirthdate(), LocalDate.now()).getYears();
            PassengerType pt = passengerTypeService.getPassengerTypeByAge(age);
            if (pt == null) throw new IllegalArgumentException("Invalid passenger type for age: " + age);
            p.setPassengerTypeId(pt.getId());

            // 2) Dispo par classe (via details, pas entêtes)
            int totalSeats = (int) airplaneService.getSeatCountByAirplaneIdClassId(
                flightService.getFlightById(header.getFlightId()).getAirplaneId(),
                p.getClassId()
            );
            int alreadyBooked = reservationRepository.countReservedSeatsByFlightAndClass(header.getFlightId(), p.getClassId());
            if (alreadyBooked >= totalSeats) {
                throw new IllegalArgumentException("No available seats in this class.");
            }

            // 3) Tarifs & promo
            FlightClassPassenger fcp = flightClassPassengerService.getFlightClassPassengerById(
                header.getFlightId(), p.getClassId(), pt.getId()
            );
            if (fcp == null) {
                throw new IllegalArgumentException("No fare configured for class " + p.getClassId() + " and passenger type " + pt.getId());
            }

            double base = fcp.getBasePrice() != null ? fcp.getBasePrice() : 0.0;
            int promoLimit = fcp.getPromotionLimit() != null ? fcp.getPromotionLimit() : 0;
            boolean promo = alreadyBooked < promoLimit;

            double discount = promo ? (fcp.getPromotionDiscount() != null ? fcp.getPromotionDiscount() : 0.0) : 0.0;
            double finalPrice = Math.max(0.0, base - discount);

            p.setBasePrice(base);
            p.setDiscount(discount);
            p.setFinalPrice(finalPrice);
            p.setPromoApplied(promo);
            p.setCreatedAt(now);

            totalAmount += finalPrice;
            totalDiscount += discount;
        }

        header.setTotalAmount(totalAmount);
        header.setTotalDiscount(totalDiscount);

        // Persist entête + lignes
        reservationRepository.saveReservationWithPassengers(header, passengers);
    }

    public List<Reservation> getReservationsByUserId(Integer userId) {
        return reservationRepository.getAllReservations()
                .stream().filter(r -> r.getUserId().equals(userId)).toList();
    }

    public void updateReservationWithPassengers(Reservation header, List<ReservationPassenger> passengers) {
        // 1) Vérifier cutoff (mêmes règles que create)
        int cutoffHours = Integer.parseInt(
            configurationService.getConfigurationByKey(ConfigKey.RESERVATION_CUTOFF_HOURS.getDatabaseKey()).getConfigValue()
        );
        var flight = flightService.getFlightById(header.getFlightId());
        var now = java.time.LocalDateTime.now();
        if (flight.getDepartureTime().minusHours(cutoffHours).isBefore(now)) {
            throw new IllegalArgumentException("Impossible de modifier la réservation à moins de " + cutoffHours + "h du départ.");
        }

        // 2) On supprime d'abord toutes les lignes existantes
        reservationRepository.deletePassengersByReservationId(header.getId());

        // 3) Recalcule tarifs/age/promo, puis insère les nouvelles lignes (comme createReservation)
        double totalAmount = 0.0;
        double totalDiscount = 0.0;

        for (ReservationPassenger p : passengers) {
            int age = java.time.Period.between(p.getPassengerBirthdate(), java.time.LocalDate.now()).getYears();
            var pt = passengerTypeService.getPassengerTypeByAge(age);
            if (pt == null) throw new IllegalArgumentException("Type passager invalide pour l'âge: " + age);
            p.setPassengerTypeId(pt.getId());

            int totalSeats = (int) airplaneService.getSeatCountByAirplaneIdClassId(flight.getAirplaneId(), p.getClassId());
            int alreadyBooked = reservationRepository.countReservedSeatsByFlightAndClass(header.getFlightId(), p.getClassId());
            if (alreadyBooked >= totalSeats) {
                throw new IllegalArgumentException("Plus de siège disponible dans cette classe.");
            }

            var fcp = flightClassPassengerService.getFlightClassPassengerById(header.getFlightId(), p.getClassId(), pt.getId());
            if (fcp == null) throw new IllegalArgumentException("Tarif indisponible pour cette classe/type de passager.");

            double base = fcp.getBasePrice() != null ? fcp.getBasePrice() : 0.0;
            int promoLimit = fcp.getPromotionLimit() != null ? fcp.getPromotionLimit() : 0;
            boolean promo = alreadyBooked < promoLimit;
            double discount = promo ? (fcp.getPromotionDiscount() != null ? fcp.getPromotionDiscount() : 0.0) : 0.0;
            double finalPrice = Math.max(0.0, base - discount);

            p.setBasePrice(base);
            p.setDiscount(discount);
            p.setFinalPrice(finalPrice);
            p.setPromoApplied(promo);
            p.setCreatedAt(now);
        }

        for (ReservationPassenger p : passengers) {
            totalAmount += p.getFinalPrice() != null ? p.getFinalPrice() : 0.0;
            totalDiscount += p.getDiscount() != null ? p.getDiscount() : 0.0;
        }

        header.setTotalAmount(totalAmount);
        header.setTotalDiscount(totalDiscount);
        header.setUpdatedAt(now);

        // 4) Sauvegarde : update header + insert lignes
        reservationRepository.updateReservation(header);
        reservationRepository.savePassengers(header.getId(), passengers);
    }
}
