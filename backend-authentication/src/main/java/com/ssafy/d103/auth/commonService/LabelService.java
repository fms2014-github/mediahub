package com.ssafy.d103.auth.commonService;

import com.ssafy.d103.auth.exception.LabelNotFoundException;
import com.ssafy.d103.auth.model.Label;
import com.ssafy.d103.auth.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LabelService {

    private final LabelRepository labelRepository;

    public Label getLabelById(Long id){
        return labelRepository.findById(id).orElseThrow(() -> new LabelNotFoundException(id));
    }

    public void setChannelsRootLabel(Label root, List channels){
        root.getChannels().addAll(channels);
        labelRepository.saveAndFlush(root);
    }

}
