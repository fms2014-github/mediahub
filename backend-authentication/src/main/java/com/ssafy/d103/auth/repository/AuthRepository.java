package com.ssafy.d103.auth.repository;

import com.ssafy.d103.auth.model.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Auth, Long> {
}
