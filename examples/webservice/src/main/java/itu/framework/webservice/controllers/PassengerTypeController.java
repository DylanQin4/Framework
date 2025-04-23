package itu.framework.webservice.controllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import itu.framework.webservice.entity.PassengerType;
import itu.framework.webservice.repository.PassengerTypeRepository;
import itu.framework.webservice.validations.AgeRangeValidator;
import itu.framework.webservice.wrapper.PassengerTypesForm;

@Controller
@RequestMapping("/admin/settings/passenger-types")
@PreAuthorize("hasRole('ADMIN')")
public class PassengerTypeController {

    private final PassengerTypeRepository repo;

    public PassengerTypeController(PassengerTypeRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public String editForm(Model model, @RequestParam(value = "saved", required = false) String saved) {
        List<PassengerType> types = repo.findAll();
        model.addAttribute("form", new PassengerTypesForm(types));
        model.addAttribute("saved", saved != null);
        return "admin/settings/passenger-types/edit";
    }

	@PostMapping
	public String save(@ModelAttribute("form") PassengerTypesForm form, BindingResult br) {
		try {
			AgeRangeValidator.validate(form.getItems());
		} catch (IllegalArgumentException ex) {
			br.reject("ageRanges", ex.getMessage());
			return "admin/settings/passenger-types/edit";
		}

		for (PassengerType pt : form.getItems()) {
			PassengerType db = repo.findById(pt.getId()).orElse(null);
			if (db != null) {
			db.setStartAge(pt.getStartAge());
			db.setEndAge(pt.getEndAge());
			repo.save(db);
			}
		}
		return "redirect:/admin/settings/passenger-types?saved=1";
	}
}
