package mg.itu.avion.mappers;

import mg.itu.avion.reservation.Reservation;

public class ReservationMapperDTO {
    public static ReservationDTO mapToDTO(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setId(reservation.getId());
        dto.setStatus(reservation.getStatus().name());
        dto.setFlightNumber(reservation.getFlightNumber());
        dto.setDepartureTime(reservation.getDepartureTime() != null ? 
                reservation.getDepartureTime().formatted(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null);
        dto.setArrivalTime(reservation.getArrivalTime() != null ? 
                reservation.getArrivalTime().formatted(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null);
        dto.setAirplane(reservation.getAirplane());
        dto.setDepartureCity(reservation.getDepartureCity());
        dto.setArrivalCity(reservation.getArrivalCity());
        dto.setTotalAmount(reservation.getTotalAmount());
        dto.setTotalDiscount(reservation.getTotalDiscount());
        dto.setCancelledAt(reservation.getCancelledAt() != null ? 
                reservation.getCancelledAt().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null);
        dto.setCreatedAt(reservation.getCreatedAt() != null ? 
                reservation.getCreatedAt().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null);
        dto.setPassengers(ReservationPassengerMapperDTO.mapToDTOList(reservation.getPassengers()));
        return dto;
    }
}
