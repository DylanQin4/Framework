package itu.framework.webservice.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "config_fares",
       uniqueConstraints = @UniqueConstraint(columnNames = "passenger_type_id"))
public class ConfigFare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "passenger_type_id", nullable = false)
    private Integer passengerTypeId;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "created_at", nullable = false,
            columnDefinition = "timestamp default current_timestamp")
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public Integer getPassengerTypeId() { return passengerTypeId; }
    public BigDecimal getPrice() { return price; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setPassengerTypeId(Integer passengerTypeId) { this.passengerTypeId = passengerTypeId; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
