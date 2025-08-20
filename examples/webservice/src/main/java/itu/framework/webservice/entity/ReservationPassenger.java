package itu.framework.webservice.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservation_passengers")
public class ReservationPassenger {
    @Id
    private Integer id;
    private String  passengerName;
    private LocalDate passengerBirthdate;
    private Integer passengerTypeId;

    private Integer classId;
    private Double  basePrice;
    private Double  discount;
    private Double  finalPrice;
    private Boolean promoApplied;

    private String  filePathPassport;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public LocalDate getPassengerBirthdate() {
        return passengerBirthdate;
    }

    public void setPassengerBirthdate(LocalDate passengerBirthdate) {
        this.passengerBirthdate = passengerBirthdate;
    }

    public Integer getPassengerTypeId() {
        return passengerTypeId;
    }

    public void setPassengerTypeId(Integer passengerTypeId) {
        this.passengerTypeId = passengerTypeId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public Boolean getPromoApplied() {
        return promoApplied;
    }

    public void setPromoApplied(Boolean promoApplied) {
        this.promoApplied = promoApplied;
    }

    public String getFilePathPassport() {
        return filePathPassport;
    }

    public void setFilePathPassport(String filePathPassport) {
        this.filePathPassport = filePathPassport;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // getters and setters
}
