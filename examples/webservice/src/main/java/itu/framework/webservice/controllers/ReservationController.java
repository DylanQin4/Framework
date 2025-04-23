package itu.framework.webservice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import itu.framework.webservice.entity.Reservation;
import itu.framework.webservice.service.CurrentUserService;
import itu.framework.webservice.service.ReservationService;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

@Controller
public class ReservationController {
  private final ReservationService reservationService;
  private final CurrentUserService currentUserService;

  public ReservationController(ReservationService reservationService, CurrentUserService currentUserService) {
    this.reservationService = reservationService;
    this.currentUserService = currentUserService;
  }

  @PreAuthorize("hasAnyRole('USER','ADMIN')")
  @GetMapping("/my-reservations")
  public String myReservations(Authentication auth, Model model) {
    String email = auth.getName();
    Integer userId = currentUserService.getCurrentUserId(email);

    List<Reservation> reservations = reservationService.getReservationsByUserId(userId);
    model.addAttribute("reservations", reservations);
    return "reservations/my";
  }
}

