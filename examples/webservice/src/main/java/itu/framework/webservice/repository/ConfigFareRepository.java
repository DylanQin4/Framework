package itu.framework.webservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import itu.framework.webservice.entity.ConfigFare;

import java.util.List;
import java.util.Optional;

public interface ConfigFareRepository extends JpaRepository<ConfigFare, Long> {
    Optional<ConfigFare> findByPassengerTypeId(Integer passengerTypeId);
    List<ConfigFare> findByPassengerTypeIdIn(List<Integer> passengerTypeIds);
}
