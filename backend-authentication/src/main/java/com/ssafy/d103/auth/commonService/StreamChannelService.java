package com.ssafy.d103.auth.commonService;

import com.ssafy.d103.auth.exception.StreamerNotRegisterException;
import com.ssafy.d103.auth.model.StreamChannel;
import com.ssafy.d103.auth.repository.StreamChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StreamChannelService {
    private final StreamChannelRepository streamChannelRepository;

    public StreamChannel saveStreamChannel(StreamChannel streamChannel){
        return streamChannelRepository.save(streamChannel);
    }

    public StreamChannel findById(String id){
        return streamChannelRepository.findById(id).orElseThrow(()-> new StreamerNotRegisterException(id));
    }


}
