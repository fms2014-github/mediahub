package com.ssafy.d103.auth.repository;

import com.ssafy.d103.auth.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LabelRepository extends JpaRepository<Label, Long> {
    Optional<Label> findByMemberId(Long id);
//    @Query(
//            value = "update labels set super_label_id = "
//    )
//    Optional<Label> updateLabelLocation(@Param() Long superId, Long subId);

}
