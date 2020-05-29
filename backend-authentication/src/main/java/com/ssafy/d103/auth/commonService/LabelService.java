package com.ssafy.d103.auth.commonService;

import com.ssafy.d103.auth.exception.LabelNotFoundException;
import com.ssafy.d103.auth.model.Label;
import com.ssafy.d103.auth.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public void updateLabelLocation(long superId, long subId){
        Label subLabel = labelRepository.findById(subId).orElseThrow(() -> new LabelNotFoundException(subId));
        Label superLabel = labelRepository.findById(subId).orElseThrow(() -> new LabelNotFoundException(superId));
        if(subLabel != null & superLabel != null){
            subLabel.setSuperLabel(superLabel);
            labelRepository.save(subLabel);
        }
    }

}
