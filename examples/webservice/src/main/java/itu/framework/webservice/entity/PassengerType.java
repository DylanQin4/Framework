package itu.framework.webservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "passenger_type")
public class PassengerType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type_name", nullable = false, length = 10)
    @NotBlank(message = "Type name is required")
    private String typeName;

    @Column(name = "start_age")
    @Min(value = 0, message = "Start age must be >= 0")
    private Integer startAge;

    @Column(name = "end_age")
    @Min(value = 0, message = "End age must be >= 0")
    private Integer endAge;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    public Integer getStartAge() { return startAge; }
    public void setStartAge(Integer startAge) { this.startAge = startAge; }
    public Integer getEndAge() { return endAge; }
    public void setEndAge(Integer endAge) { this.endAge = endAge; }
}
