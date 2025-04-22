package itu.framework.webservice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
public class AdminController {

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/admin/parametrage")
  public String settings() {
    return "admin/parametrage";
  }
}

