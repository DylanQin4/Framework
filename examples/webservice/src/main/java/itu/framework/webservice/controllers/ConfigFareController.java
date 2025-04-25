package itu.framework.webservice.controllers;

import itu.framework.webservice.service.ConfigFareService;
import itu.framework.webservice.wrapper.ConfigFaresForm;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ConfigFareController {

    private final ConfigFareService configFareService;

    public ConfigFareController(ConfigFareService configFareService) {
        this.configFareService = configFareService;
    }

    @GetMapping("/admin/settings/config-fares")
    public String edit(Model model, Boolean saved) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", configFareService.buildForm());
        }
        if (saved != null && saved) {
            model.addAttribute("saved", true);
        }
        model.addAttribute("pageTitle", "Paramétrage - Tarifs par type");
        return "admin/settings/config-fares/edit";
    }

    @PostMapping("/admin/settings/config-fares")
    public String update(@Valid ConfigFaresForm form, BindingResult binding, Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("pageTitle", "Paramétrage - Tarifs par type");
            return "admin/settings/config-fares/edit";
        }
        configFareService.saveForm(form);
        return "redirect:/admin/settings/config-fares?saved=true";
    }
}