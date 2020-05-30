package com.ssafy.d103.auth.commonService;

import com.ssafy.d103.auth.exception.LabelNotFoundException;
import com.ssafy.d103.auth.model.Channel;
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

    public void updateLabelInformation(long labelId, String labelName){
        Label label = labelRepository.findById(labelId).orElseThrow(()-> new LabelNotFoundException(labelId));
        if(label != null){
            label.setLabelName(labelName);
            labelRepository.save(label);
        }
    }

    public void deleteLabel(long labelId) {
        Label targetLabel = labelRepository.findById(labelId).orElseThrow(()->new LabelNotFoundException(labelId));
        if(targetLabel != null && targetLabel.getMemberId() == null){
            Label superLabel = targetLabel.getSuperLabel();
            targetLabel.getSubLabels().stream()
                    .map(label -> {
                        label.setSuperLabel(superLabel);
                        return label;
                    })
                    .forEach((Label label) -> superLabel.getSubLabels().add(label));
            targetLabel.getChannels().stream()
                    .forEach((Channel channel) -> superLabel.getChannels().add(channel));
            labelRepository.save(superLabel);
            labelRepository.delete(targetLabel);
        }
    }
}
