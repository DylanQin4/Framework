package itu.framework.webservice.dto;

public class PassengerDto {
    public String passengerName;
    public String passengerBirthdate; // "yyyy-MM-dd"
    public String passengerType;      // ADULT/CHILD/...
    public String className;          // BUSINESS/ECONOMY
    public Double basePrice;
    public Double discount;
    public Double finalPrice;
    public Boolean promoApplied;
}
