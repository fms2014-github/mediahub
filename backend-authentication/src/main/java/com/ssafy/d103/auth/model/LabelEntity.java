package com.ssafy.d103.auth.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "label")
public class LabelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "label_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String labelName;

    @OneToMany
    @JoinColumn(name = "label_id")
    private List<LabelEntity> labelList;

    @OneToMany
    @JoinColumn(name = "channel_id")
    private List<ChannelEntity> followChannelList;

}