package com.ssafy.d103.auth.repository;

import com.ssafy.d103.auth.model.Label;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LabelRepositoryTest {

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void repoTest(){
        labelRepository.deleteAll();
        Label label1 = new Label("루트 라벨");
        label1.getSubLabels().add(new Label("서브 라벨1"));
        label1.getSubLabels().add(new Label("서브 라벨2"));
        labelRepository.saveAndFlush(label1);
        List<Label> list = labelRepository.findAll();
        System.out.println("**********************************************************************************************");
        for(Label l : list){
            System.out.println(l.getLabelName());
        }
        System.out.println("**********************************************************************************************");

        Label subLabel = labelRepository.getOne(6L);

        subLabel.getSubLabels().add(new Label("서브1의 서브"));
        labelRepository.saveAndFlush(subLabel);

        list = labelRepository.findAll();
        System.out.println("**********************************************************************************************");
        for(Label l : list){

            System.out.println(l.getLabelName());
        }


    }
}