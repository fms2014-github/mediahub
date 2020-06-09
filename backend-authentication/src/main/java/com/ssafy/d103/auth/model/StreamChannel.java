package com.ssafy.d103.auth.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "stream_channel")
public class StreamChannel {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String channelId;

}
