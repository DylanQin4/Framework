package itu.framework.webservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import itu.framework.webservice.entity.PassengerType;

public interface PassengerTypeRepository extends JpaRepository<PassengerType, Integer> {
}
