package com.ssafy.d103.auth.repository;

import com.ssafy.d103.auth.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LabelRepository extends JpaRepository<Label, Long> {
    Optional<Label> findByMemberId(Long id);
}
