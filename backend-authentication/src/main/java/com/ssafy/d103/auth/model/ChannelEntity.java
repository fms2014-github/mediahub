package com.ssafy.d103.auth.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "channel")
public class ChannelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "CHANNEL_ID")
    private Long id;

    @Column(length = 100, nullable = false)
    private String provider;

    @Override
    public String toString() {
        return "ChannelEntity{" +
                "id=" + id +
                ", provider='" + provider + '\'' +
                '}';
    }
}