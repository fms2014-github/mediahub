package com.ssafy.d103.auth.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "channel")
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String provider;

    @Column(name = "channel_id", nullable = false)
    private String channelId;

    @Column
    private String profileImg;

    @Column
    private Long follower;

    @Column
    private Long subscriber;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Override
    public String toString() {
        return "ChannelEntity{" +
                "id=" + id +
                ", provider='" + provider + '\'' +
                '}';
    }
}