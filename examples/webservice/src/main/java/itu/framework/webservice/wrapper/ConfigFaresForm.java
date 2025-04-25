package itu.framework.webservice.wrapper;

import java.util.ArrayList;
import java.util.List;

import itu.framework.webservice.dto.FareItemDto;
import jakarta.validation.Valid;

public class ConfigFaresForm {

    @Valid
    private List<FareItemDto> items = new ArrayList<>();

    public List<FareItemDto> getItems() { return items; }
    public void setItems(List<FareItemDto> items) { this.items = items; }
}