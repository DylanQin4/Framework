package itu.framework.webservice.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationDto {
    public Integer id;
    public String status;
    public String flightNumber;
    public String departureTime; // "2025-09-20 00:00:00"
    public String arrivalTime;   // "2025-09-22 04:00:00"
    public String airplane;
    public String departureCity;
    public String arrivalCity;
    public Double totalAmount;
    public Double totalDiscount;
    public LocalDateTime createdAt;
    public List<PassengerDto> passengers;
}

