package mg.itu.avion.flight.search;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
public class FlightSearchCriteria {
    private Integer departureCityId;
    private Integer arrivalCityId;

    // Saisie par l'utilisateur (dates "jour")
    private LocalDate departureDateFrom;
    private LocalDate departureDateTo;

    // Normalisées en contrôleur pour la requête
    private LocalDateTime departureFromDateTime;
    private LocalDateTime departureToDateTime;

    // Filtres optionnels
    private Integer classId;
    private Integer passengerTypeId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Boolean promoOnly;
}