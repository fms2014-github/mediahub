package com.rest.api.repository;

import java.util.Optional;

import com.rest.api.model.MemberEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByEmail(String userEmail);
    Optional<MemberEntity> findByEmailAndProvider(String email, String provider);
}