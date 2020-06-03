package com.ssafy.d103.auth.dto;

import com.ssafy.d103.auth.model.Channel;
import com.ssafy.d103.auth.model.Label;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LabelDto {
    private Long id;
    private Long memberId;
    private String labelName;
    private Long superId;
    private Collection<Channel> channels = new ArrayList<Channel>();

    public LabelDto(Label label){
        this.id = label.getId();
        this.memberId = label.getMemberId();
        this.labelName = label.getLabelName();
        this.superId = label.getSuperLabel().getId();
        this.channels = label.getChannels();
    }
}
