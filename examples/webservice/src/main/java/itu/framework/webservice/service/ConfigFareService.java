package itu.framework.webservice.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import itu.framework.webservice.dto.FareItemDto;
import itu.framework.webservice.entity.ConfigFare;
import itu.framework.webservice.entity.PassengerType;
import itu.framework.webservice.repository.ConfigFareRepository;
import itu.framework.webservice.repository.PassengerTypeRepository;
import itu.framework.webservice.wrapper.ConfigFaresForm;

@Service
public class ConfigFareService {

    private final ConfigFareRepository configFareRepository;
    private final PassengerTypeRepository passengerTypeRepository;

    public ConfigFareService(ConfigFareRepository configFareRepository,
                             PassengerTypeRepository passengerTypeRepository) {
        this.configFareRepository = configFareRepository;
        this.passengerTypeRepository = passengerTypeRepository;
    }

    /** Construit le formulaire à partir des types de passagers + tarifs existants */
    @Transactional(readOnly = true)
    public ConfigFaresForm buildForm() {
        List<PassengerType> types = passengerTypeRepository.findAll();
        ConfigFaresForm form = new ConfigFaresForm();
        List<FareItemDto> items = new ArrayList<>();

        for (PassengerType pt : types) {
            FareItemDto dto = new FareItemDto();
            dto.setPassengerTypeId(pt.getId());
            dto.setTypeName(pt.getTypeName());

            configFareRepository.findByPassengerTypeId(pt.getId()).ifPresentOrElse(f -> {
                dto.setId(f.getId());
                dto.setPrice(f.getPrice());
            }, () -> {
                // défaut 0.00 si non configuré
                dto.setPrice(new BigDecimal("0.00"));
            });

            items.add(dto);
        }
        form.setItems(items);
        return form;
    }

    /** Sauvegarde (upsert) des prix par type de passager */
    @Transactional
    public void saveForm(ConfigFaresForm form) {
        for (FareItemDto dto : form.getItems()) {
            ConfigFare fare = configFareRepository.findByPassengerTypeId(dto.getPassengerTypeId())
                .orElseGet(ConfigFare::new);

            fare.setPassengerTypeId(dto.getPassengerTypeId());
            fare.setPrice(dto.getPrice());

            if (fare.getId() == null) {
                fare.setCreatedAt(LocalDateTime.now());
            }
            configFareRepository.save(fare);
        }
    }
}