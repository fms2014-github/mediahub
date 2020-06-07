package com.ssafy.d103.auth.controller;

import com.ssafy.d103.auth.commonService.ChannelService;
import com.ssafy.d103.auth.commonService.LabelService;
import com.ssafy.d103.auth.commonService.StreamChannelService;
import com.ssafy.d103.auth.dto.*;
import com.ssafy.d103.auth.model.*;
import com.ssafy.d103.auth.security.CurrentUser;
import com.ssafy.d103.auth.security.CustomUserDetailsService;
import com.ssafy.d103.auth.security.UserPrincipal;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Api(tags = {"user"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/member")
public class MemberController {

    private final CustomUserDetailsService memberService;
    private final LabelService labelService;
    private final ChannelService channelService;
    private final StreamChannelService streamChannelService;
    /**
     * 기본 멤버 정보, 라벨
     *
     * @param userPrincipal
     * @return
     */
    @ApiOperation(value = "유저 정보 요청")
    @GetMapping("/information")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MemberDto> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        Member member = memberService.loadMemberById(userPrincipal.getId());
        List<LabelDto> label = new LinkedList<>();
        LinkedList<Label> queue = new LinkedList<>();
        Label rootLabel = labelService.getLabelById(member.getRootLabelId());
        queue.add(rootLabel);
        while(!queue.isEmpty()){
            int size = queue.size();
            for(int i=0; i<size; i++){
                Label temp = queue.poll();
                queue.addAll(temp.getSubLabels());
                label.add(new LabelDto(temp));
            }
        }
        MemberDto memberDto = new MemberDto(member, label);
        return new ResponseEntity(memberDto, HttpStatus.OK);
    }

    @ApiOperation(value = "라벨 위치 변경 요청")
    @PutMapping("/label/location")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity updateLabelLocation(@RequestBody UpdateLabelLocationDto labelId, @CurrentUser UserPrincipal userPrincipal){
        System.out.println("========라벨 위치 변경=======");
        labelService.updateLabelLocation(Long.parseLong(labelId.getSuperLabelId()), Long.parseLong(labelId.getSubLabelId()));
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "라벨 정보 변경 요청")
    @PutMapping("/label")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity updateLabelInformation(@RequestParam String labelId, @RequestParam String labelName, @CurrentUser UserPrincipal userPrincipal){
        System.out.println("========라벨 변경=======");
        labelService.updateLabelInformation(Long.parseLong(labelId), labelName);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "라벨 삭제 요청")
    @DeleteMapping("/label")
    public ResponseEntity deleteLabel(@RequestParam String labelId, @CurrentUser UserPrincipal userPrincipal){
        System.out.println("========라벨 삭제=======");
        labelService.deleteLabel(Long.parseLong(labelId));
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "라벨 생성")
    @PostMapping("/label")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity createLabel(@RequestParam String labelId, @RequestParam String labelName, @CurrentUser UserPrincipal userPrincipal){
        System.out.println("========라벨 생성=======");
        labelService.createLabel(Long.parseLong(labelId), labelName, userPrincipal.getId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "채널 생성")
    @PostMapping("/channel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ChannelDto> createChannel(@RequestBody Channel channel, @RequestParam String labelId, @CurrentUser UserPrincipal userPrincipal){
        return new ResponseEntity(new ChannelDto(channelService.createNewChannel(Long.parseLong(labelId), channel)), HttpStatus.OK);
    }

    @ApiOperation(value = "채널 삭제")
    @DeleteMapping("/channel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity deleteChannel(@RequestParam String channelId, @CurrentUser UserPrincipal userPrincipal){
        channelService.deleteChannel(Long.parseLong(channelId));
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "채널 카테고리 변경")
    @PutMapping("/channel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity changeLabel(@RequestParam String channelId, @RequestParam String labelId, @CurrentUser UserPrincipal userPrincipal){
        Label label = labelService.getLabelById(Long.parseLong(labelId));
        Channel channel = channelService.findById(Long.parseLong(channelId));
        channel.setLabel(label);
        channelService.saveChannel(channel);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "FirstLogin 증가")
    @PutMapping("/addFirstLogin")
    public ResponseEntity addFirst(@CurrentUser UserPrincipal userPrincipal){
        Member member = memberService.loadMemberById(userPrincipal.getId());
        member.setFirstLogin(member.getFirstLogin()+1);
        memberService.saveMember(member);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "FirstLogin 감소")
    @PutMapping("/subFirstLogin")
    public ResponseEntity subFirst(@CurrentUser UserPrincipal userPrincipal){
        Member member = memberService.loadMemberById(userPrincipal.getId());
        member.setFirstLogin(member.getFirstLogin()-1);
        memberService.saveMember(member);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "유투브나 트위치 채널 이름 혹은 채널 아이디로 모든 방송 조회 " +
            "channelId : youtube인 경우는 channel Id, twitch인 경우는 twitch channel name")
    @GetMapping("/stream-channel")
    public ResponseEntity getStreamChannelByChannelIdOrChannelName(@RequestParam String channelId, @RequestParam String provider){
        StreamChannel streamChannel = null;
        if(provider.equals("youtube")){
            streamChannel = streamChannelService.findById("Y.".concat(channelId));
        }else if(provider.equals("twitch")){
            streamChannel = streamChannelService.findById("T.".concat(channelId));
        }

        if(streamChannel != null){
            Member member = memberService.loadMemberById(streamChannel.getMember().getId());
            List<StreamChannelDto> list = member.getStreamChannel().stream()
                    .map(StreamChannel -> {
                        StreamChannelDto streamChannelDto = new StreamChannelDto();
                        if(StreamChannel.getId().charAt(0) == 'Y'){
                            streamChannelDto.setProvider("youtube");
                        }else if(StreamChannel.getId().charAt(0) == 'T'){
                            streamChannelDto.setProvider("twitch");
                        }
                        streamChannelDto.setChannelId(StreamChannel.getId().substring(2));
                        return streamChannelDto;
                    })
                    .collect(Collectors.toList());
            return new ResponseEntity(list, HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }
}
