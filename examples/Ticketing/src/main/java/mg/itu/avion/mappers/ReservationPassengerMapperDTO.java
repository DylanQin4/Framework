package mg.itu.avion.mappers;

import java.util.List;

import mg.itu.avion.reservation.ReservationPassenger;

public class ReservationPassengerMapperDTO {
    public static ReservationPassengerDTO mapToDTO(ReservationPassenger p) {
        if (p == null) return null;
        ReservationPassengerDTO dto = new ReservationPassengerDTO();
        dto.setPassengerName(p.getPassengerName());
        dto.setPassengerBirthdate(p.getPassengerBirthdate() != null ? p.getPassengerBirthdate().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE) : null);
        dto.setPassengerType(p.getPassengerType());
        dto.setClassName(p.getClassName());
        dto.setBasePrice(p.getBasePrice());
        dto.setDiscount(p.getDiscount());
        dto.setFinalPrice(p.getFinalPrice());
        dto.setPromoApplied(p.getPromoApplied());
        return dto;
    }

    public static List<ReservationPassengerDTO> mapToDTOList(List<ReservationPassenger> passengers) {
        if (passengers == null) return null;
        return passengers.stream()
                .map(ReservationPassengerMapperDTO::mapToDTO)
                .toList();
    }
}
