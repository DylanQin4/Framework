package itu.framework.webservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import itu.framework.webservice.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u.id FROM User u WHERE u.email = ?1")
    Integer findIdByEmail(String email);
}
