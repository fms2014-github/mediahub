package com.ssafy.d103.auth.commonService;

import com.ssafy.d103.auth.exception.ChannelNotFoundException;
import com.ssafy.d103.auth.exception.LabelNotFoundException;
import com.ssafy.d103.auth.model.Channel;
import com.ssafy.d103.auth.model.Label;
import com.ssafy.d103.auth.repository.ChannelRepository;
import com.ssafy.d103.auth.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final LabelRepository labelRepository;
    private final ChannelRepository channelRepository;

    public Channel createNewChannel(long labelId, Channel channel){
        Label label = labelRepository.findById(labelId).orElseThrow(()-> new LabelNotFoundException(labelId));
        channel.setLabel(label);
        return channelRepository.save(channel);
    }

    public void deleteChannel(long channelId){
        channelRepository.deleteById(channelId);
    }

    public Channel saveChannel(Channel channel){
        return channelRepository.save(channel);
    }

    public void saveAll(List<Channel> channels){
        channelRepository.saveAll(channels);
    }

    public List<Channel> findAllChannelByLabelId(long id){
        return channelRepository.findAllByLabel_Id(id).orElseThrow(()->new ChannelNotFoundException(id));
    }

    public Channel findById(long id){
        return channelRepository.findById(id).orElseThrow(()-> new ChannelNotFoundException(id));
    }
    public Channel findByChannelId(String channelId){
        return channelRepository.findAllByChannelId(channelId);
    }
}
