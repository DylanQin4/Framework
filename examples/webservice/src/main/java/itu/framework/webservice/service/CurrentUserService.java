package itu.framework.webservice.service;

import org.springframework.stereotype.Service;

import itu.framework.webservice.repository.UserRepository;

@Service
public class CurrentUserService {
  private final UserRepository repo;
  public CurrentUserService(UserRepository repo) { this.repo = repo; }

  public Integer getCurrentUserId(String email) {
    return repo.findIdByEmail(email);
  }
}
