package com.ssafy.d103.auth.commonService;

import com.ssafy.d103.auth.exception.LabelNotFoundException;
import com.ssafy.d103.auth.model.Channel;
import com.ssafy.d103.auth.model.Label;
import com.ssafy.d103.auth.repository.ChannelRepository;
import com.ssafy.d103.auth.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final LabelRepository labelRepository;
    private final ChannelRepository channelRepository;

    public void createNewChannel(long labelId, Channel channel){
        Label label = labelRepository.findById(labelId).orElseThrow(()-> new LabelNotFoundException(labelId));
        label.getChannels().add(channel);
        labelRepository.save(label);
    }

    public void deleteChannel(long channelId){
        channelRepository.deleteById(channelId);
    }

    public Channel saveChannel(Channel channel){
        return channelRepository.save(channel);
    }

}
