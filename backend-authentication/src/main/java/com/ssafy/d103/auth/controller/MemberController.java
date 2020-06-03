package com.ssafy.d103.auth.controller;

import com.ssafy.d103.auth.commonService.ChannelService;
import com.ssafy.d103.auth.commonService.LabelService;
import com.ssafy.d103.auth.dto.AuthDto;
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
        System.out.println("information");
        Member member = memberService.loadMemberById(userPrincipal.getId());
        StringBuilder sb = new StringBuilder();
        sb.append("\"{");
        sb.append(" \"member\" :  {");
        sb.append(" \"name\" : \""); sb.append(member.getName()); sb.append("\",");
        sb.append(" \"email\" : \""); sb.append(member.getEmail()); sb.append("\",");
        sb.append(" \"provider\" : \""); sb.append(member.getProvider()); sb.append("\",");
        sb.append(" \"profileUrl\" : \""); sb.append(member.getProfileUrl()); sb.append("\",");
        sb.append(" \"providerId\" : \""); sb.append(member.getProviderId()); sb.append("\",");
        sb.append(" \"firstLogin\" : \""); sb.append(member.getFirstLogin()); sb.append("\",");
        sb.append(" \"roles\" : \""); sb.append(member.getRole()); sb.append("\",");
        sb.append(" \"auth\" : [");
        Iterator<Auth> it = member.getAuth().iterator();
        while(it.hasNext()){
            Auth auth = it.next();
            sb.append("{");
            sb.append(" \"access_token\" : \""); sb.append(auth.getAccess_token()); sb.append("\",");
            sb.append(" \"provider\" : \""); sb.append(auth.getAuth_provider()); sb.append("\"");
            sb.append("}");
            if(it.hasNext()) sb.append(",");
        }
        sb.append("],");
        sb.append(" \"labels\" : [");
        LinkedList<Label> queue = new LinkedList<>();
        Label rootLabel = labelService.getLabelById(member.getRootLabelId());
        queue.add(rootLabel);
        while(!queue.isEmpty()){

            int size = queue.size();

            for(int i=0; i<size; i++){
                Label temp = queue.poll();

                queue.addAll(temp.getSubLabels());

                sb.append("{");
                sb.append(" \"id\" : \""); sb.append(temp.getId()); sb.append("\",");
                sb.append(" \"memberId\" : \""); sb.append(temp.getMemberId()); sb.append("\",");
                sb.append(" \"labelName\" : \""); sb.append(temp.getLabelName()); sb.append("\",");
                sb.append(" \"superLabel\" : \"");
                if(temp.getSuperLabel() != null) sb.append(temp.getSuperLabel().getId());
                sb.append("\",");

                sb.append(" \"channel\" : [");
                Iterator<Channel> channels = temp.getChannels().iterator();
                while(channels.hasNext()){
                    Channel channel = channels.next();
                    sb.append("{");
                    sb.append(" \"id\" : \""); sb.append(channel.getId()); sb.append("\",");
                    sb.append(" \"labelId\" : \""); sb.append(channel.getLabel().getId()); sb.append("\",");
                    sb.append(" \"provider\" : \""); sb.append(channel.getProvider()); sb.append("\",");
                    sb.append(" \"channelId\" : \""); sb.append(channel.getChannelId()); sb.append("\",");
                    sb.append(" \"name\" : \""); sb.append(channel.getName()); sb.append("\",");
                    sb.append("}");
                    if(channels.hasNext()){
                        sb.append(",");
                    }
                }
                sb.append("]");
                sb.append("}");

                if(!queue.isEmpty()) sb.append(",");
            }
        }
        sb.append("]");
        sb.append("}");
        sb.append("}\"");
        System.out.println(sb.toString());
        return new ResponseEntity(sb.toString(), HttpStatus.OK);
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
