package itu.framework.webservice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
public class ReservationController {

  @PreAuthorize("hasAnyRole('USER','ADMIN')")
  @GetMapping("/reservations/mes")
  public String myReservations() {
    return "reservations/mes";
  }
}

