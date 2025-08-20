package itu.framework.webservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import itu.framework.webservice.entity.Reservation;
import itu.framework.webservice.repository.ReservationRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getReservationsByUserId(Integer userId) {
        return reservationRepository.findByUserId(userId);
    }
}
