package com.ssafy.d103.auth.repository;

import com.ssafy.d103.auth.model.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findById(Long id);
    void deleteById(Long id);
}
