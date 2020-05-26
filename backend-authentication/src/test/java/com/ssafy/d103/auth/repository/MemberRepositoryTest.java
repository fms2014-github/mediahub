package com.ssafy.d103.auth.repository;

import com.ssafy.d103.auth.model.AuthProvider;
import com.ssafy.d103.auth.model.LabelEntity;
import com.ssafy.d103.auth.model.MemberEntity;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void memberRepotest(){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setEmail("atlanboa");
        memberEntity.setFirstLogin(0);
        memberEntity.setName("김상헌");
        memberEntity.setProvider(AuthProvider.google);



        memberRepository.save(memberEntity);

        Optional<MemberEntity> member = memberRepository.findByEmail("atlanboa");

        LabelEntity label1 = new LabelEntity();
        label1.setLabelName("루트 라벨");
        label1.setMember(member.get());

        memberEntity.addLabel(label1);

        memberRepository.save(memberEntity);
        member = memberRepository.findByEmail("atlanboa");

        memberEntity = member.get();
        //루트 라벨 추가된 상태



        //멤버 id랑 root 라벨 id로 찾아와봄
        Optional<LabelEntity> rootLabel = labelRepository.findById(memberEntity.getLabelList().get(0).getId());
        LabelEntity checkRootLabel = rootLabel.get();

        // 라벨 추가할때 멤버 id랑 라벨 id아래에 추가하게 해주는거니깐 멤버id랑 라벨 id로 라벨 찾아와서 라벨 넣으면되겠네

        LabelEntity label2 = new LabelEntity();
        label2.setLabelName("라벨1의 자식1");
        label2.setMember(memberEntity);
        label2.setParent(checkRootLabel);

        LabelEntity label3 = new LabelEntity();
        label3.setLabelName("라벨1의 자식2");
        label3.setMember(memberEntity);
        label3.setParent(checkRootLabel);

        labelRepository.save(label2);
        labelRepository.save(label3);



//        LabelEntity label4 = new LabelEntity();
//        label4.setLabelName("라벨 1의 자식1의 손자1");
//
//
//        label1.setMember(memberEntity);


        member = memberRepository.findByEmail("atlanboa");

        MemberEntity checkMember = member.get();



        memberRepository.save(checkMember);
        member = memberRepository.findByEmail("atlanboa");
        MemberEntity checkMember2 = member.get();

        System.out.println(checkMember2);



//        resultMember.getLabelList().add(label1);

//        MemberEntity memberEntity2 = new MemberEntity().builder()
//                .email("atlanboa2")
//                .firstLogin(0)
//                .name("김상헌")
//                .provider(AuthProvider.google)
//                .labelList(labelList)
//                .build();
//
//        memberRepository.save(memberEntity);
//
//        member = memberRepository.findByEmail("atlanboa2");
//
//        resultMember = member.get();
//        System.out.println(resultMember);
//
//        for(LabelEntity label : resultMember.getLabelList()){
//            System.out.println(label);
//        }




    }
}