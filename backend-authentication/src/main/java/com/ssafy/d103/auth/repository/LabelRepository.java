package com.ssafy.d103.auth.repository;

import com.ssafy.d103.auth.model.LabelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<LabelEntity, Long> {
}
