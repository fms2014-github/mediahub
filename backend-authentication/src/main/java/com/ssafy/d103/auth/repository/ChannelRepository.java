package com.ssafy.d103.auth.repository;

import com.ssafy.d103.auth.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    Optional<List<Channel>> findAllByLabel_Id(long id);
    Channel findAllByChannelId(String channelId);
}
