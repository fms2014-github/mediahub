package com.ssafy.d103.auth.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "channel")
public class Channel implements Comparable<Channel> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "label_id")
    private Label label;

    @Column(length = 100, nullable = false)
    private String provider;

    @Column(name = "channel_id", nullable = false)
    private String channelId;

    @Column
    private String name;

    @Column String displayName;

    @Column
    private String profileImg;

    @Column
    private Long follower;

    @Column
    private String subscriptionId;

    @Column
    private Long subscriber;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Override
    public String toString() {
        return "ChannelEntity{" +
                "id=" + id +
                ", channelId='" + channelId + '\'' +
                ", name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", provider='" + provider + '\'' +
                "}\n";
    }

    @Override
    public int compareTo(Channel o) {
        return this.name.compareTo(o.getName());
    }
}