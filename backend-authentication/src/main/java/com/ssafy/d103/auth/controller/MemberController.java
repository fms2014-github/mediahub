package com.ssafy.d103.auth.controller;

import com.ssafy.d103.auth.commonService.ChannelService;
import com.ssafy.d103.auth.commonService.LabelService;
import com.ssafy.d103.auth.dto.AuthDto;
import com.ssafy.d103.auth.dto.LabelDto;
import com.ssafy.d103.auth.dto.MemberDto;
import com.ssafy.d103.auth.dto.UpdateLabelLocationDto;
import com.ssafy.d103.auth.model.Auth;
import com.ssafy.d103.auth.model.Channel;
import com.ssafy.d103.auth.model.Label;
import com.ssafy.d103.auth.model.Member;
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
    /**
     * 기본 멤버 정보, 라벨
     *
     * @param userPrincipal
     * @return
     */
    @ApiOperation(value = "유저 정보 요청")
    @GetMapping("/information")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        Member member = memberService.loadMemberById(userPrincipal.getId());
        List<LabelDto> label = new LinkedList<>();
        LinkedList<Label> queue = new LinkedList<>();
        Label rootLabel = labelService.getLabelById(member.getRootLabelId());
        queue.add(rootLabel);
        while(queue.isEmpty()){
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
    public ResponseEntity updateLabelLocation(@RequestBody UpdateLabelLocationDto labelId){
        labelService.updateLabelLocation(Long.parseLong(labelId.getSuperLabelId()), Long.parseLong(labelId.getSubLabelId()));
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "라벨 정보 변경 요청")
    @PutMapping("/label")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity updateLabelInformation(@RequestParam String labelId, @RequestParam String labelName){
        labelService.updateLabelInformation(Long.parseLong(labelId), labelName);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "라벨 삭제 요청")
    @DeleteMapping("/label")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity deleteLabel(@RequestParam String labelId){
        labelService.deleteLabel(Long.parseLong(labelId));
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "라벨 생성")
    @PostMapping("/label")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity createLabel(@RequestParam String labelId, @RequestParam String labelName){
        labelService.createLabel(Long.parseLong(labelId), labelName);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "채널 생성")
    @PostMapping("/channel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Channel> createChannel(@RequestBody Channel channel, @RequestParam String labelId){
        return new ResponseEntity(channelService.createNewChannel(Long.parseLong(labelId), channel), HttpStatus.OK);
    }

    @ApiOperation(value = "채널 삭제")
    @DeleteMapping("/channel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity deleteChannel(@RequestParam String channelId){
        channelService.deleteChannel(Long.parseLong(channelId));
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "채널 카테고리 변경")
    @PutMapping("/channel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity changeLabel(@RequestParam String channelId, @RequestParam String labelId){
        Label label = labelService.getLabelById(Long.parseLong(labelId));
        Channel channel = channelService.findById(Long.parseLong(channelId));
        channel.setLabel(label);
        channelService.saveChannel(channel);
        return new ResponseEntity(HttpStatus.OK);
    }

}
