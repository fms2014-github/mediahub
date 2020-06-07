package com.ssafy.d103.auth.repository;

import com.ssafy.d103.auth.model.StreamerMultiPlatform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StreamerMultiPlatformRepository extends JpaRepository<StreamerMultiPlatform, Long> {
    Optional<StreamerMultiPlatform> findById(Long id);
    Optional<StreamerMultiPlatform> findByYouTubeChannelId(String youTubeChannelId);
    Optional<StreamerMultiPlatform> findByTwitchId(String twitchId);
}
