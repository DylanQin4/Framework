package itu.framework.webservice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  @GetMapping("/")
  public String home() {
    // redirect to reservations page
    return "redirect:/my-reservations";
  }
}

