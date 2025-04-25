package itu.framework.webservice.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class FareItemDto {
    private Long id;

    @NotNull
    private Integer passengerTypeId;

    private String typeName;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true, message = "Le prix doit être >= 0")
    private BigDecimal price;

    public Long getId() { return id; }
    public Integer getPassengerTypeId() { return passengerTypeId; }
    public String getTypeName() { return typeName; }
    public BigDecimal getPrice() { return price; }

    public void setId(Long id) { this.id = id; }
    public void setPassengerTypeId(Integer passengerTypeId) { this.passengerTypeId = passengerTypeId; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    public void setPrice(BigDecimal price) { this.price = price; }
}

