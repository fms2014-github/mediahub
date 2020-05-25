package com.ssafy.d103.auth.repository;

import com.ssafy.d103.auth.model.LabelEntity;
import com.ssafy.d103.auth.model.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LabelRepository extends JpaRepository<LabelEntity, Long> {
}
