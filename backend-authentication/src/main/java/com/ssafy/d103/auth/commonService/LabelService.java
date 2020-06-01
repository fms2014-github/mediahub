package com.ssafy.d103.auth.commonService;

import com.ssafy.d103.auth.exception.LabelNotFoundException;
import com.ssafy.d103.auth.model.Channel;
import com.ssafy.d103.auth.model.Label;
import com.ssafy.d103.auth.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Null;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LabelService {

    private final LabelRepository labelRepository;

    public Label getLabelById(long id){
        return labelRepository.findById(id).orElseThrow(() -> new LabelNotFoundException(id));
    }

    public void setChannelsRootLabel(Label root, List channels){
        root.getChannels().addAll(channels);
        labelRepository.save(root);
    }

    public void updateLabelLocation(long superId, long subId){
        Label subLabel = labelRepository.findById(subId).orElseThrow(() -> new LabelNotFoundException(subId));
        Label superLabel = labelRepository.findById(superId).orElseThrow(() -> new LabelNotFoundException(superId));
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

            Iterator<Label> it = targetLabel.getSubLabels().iterator();
            while(it.hasNext()){
                Label label = it.next();
                label.setSuperLabel(superLabel);
                superLabel.getSubLabels().add(label);
            }
            superLabel.getChannels().addAll(targetLabel.getChannels());
            targetLabel.setSuperLabel(null);
            targetLabel.setSubLabels(null);
            labelRepository.delete(targetLabel);
            labelRepository.saveAndFlush(superLabel);
        }
    }

    public void createLabel(long labelId, String labelName){
        Label superLabel = labelRepository.findById(labelId).orElseThrow(()-> new LabelNotFoundException(labelId));
        Label label = new Label();
        label.setLabelName(labelName);
        label.setSuperLabel(superLabel);
        labelRepository.save(label);
    }
}
