package itu.framework.webservice.wrapper;

import java.util.List;
import itu.framework.webservice.entity.PassengerType;

public class PassengerTypesForm {
    private List<PassengerType> items;

    public PassengerTypesForm() {}
    public PassengerTypesForm(List<PassengerType> items) { this.items = items; }

    public List<PassengerType> getItems() { return items; }
    public void setItems(List<PassengerType> items) { this.items = items; }
}
