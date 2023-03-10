package com.secondproject.coupleaccount.api.member;


import java.io.File;
import java.nio.channels.MembershipKey;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.secondproject.coupleaccount.api.config.JwtUtil;
import com.secondproject.coupleaccount.api.config.LoginUserSeq;
import com.secondproject.coupleaccount.service.BackgroundImgService;
import com.secondproject.coupleaccount.service.MailService;
import com.secondproject.coupleaccount.service.MemberService;
import com.secondproject.coupleaccount.vo.LoginMemberVO;
import com.secondproject.coupleaccount.vo.MemberBasicAccountInfoVO;
import com.secondproject.coupleaccount.vo.MemberInfoChangeVO;
import com.secondproject.coupleaccount.vo.MemberJoinVo;
import com.secondproject.coupleaccount.vo.member.FindPwdResponseVO;
import com.secondproject.coupleaccount.vo.member.FindPwdVO;
import com.secondproject.coupleaccount.vo.member.LoginResponseInfoVO;
import com.secondproject.coupleaccount.vo.member.LoginResponseVO;
import com.secondproject.coupleaccount.vo.member.MemberDuplicatedResponseVO;
import com.secondproject.coupleaccount.vo.member.MemberDuplicatedVO;
import com.secondproject.coupleaccount.vo.member.MemberJoinResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;


@Tag(name = "??????", description = "?????? ?????? API")
@Slf4j
@RequestMapping("/api/member")
@RestController
public class MemberAPIController {
    @Autowired MemberService memberService;
    @Autowired JwtUtil jwtUtil;
    @Autowired MailService eMailService;

    // @PostMapping("/findpwd")
    // @ResponseBody
    // public String mailConfirm(@RequestParam String email) throws Exception {
    //     String code = eMailService.sendSimpleMessage(email);
    //     log.info("???????????? : " + code);
    //     return code;
    // }

    // @PostMapping("/findpwd")
    // public ResponseEntity<FindPwdResponseVO> userFindPwd(@RequestBody FindPwdVO data) {

    // }

    // @Operation(summary = "?????? ?????????", description = "????????? ??????")
    // @PostMapping("/login")
    // public ResponseEntity<LoginResponseVO> userLogin(@RequestBody LoginMemberVO data){
    //     LoginResponseVO respnse = memberService.loginUser(data);
    //     return new ResponseEntity<>(respnse, HttpStatus.OK);
    // }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        Map<String, Object> map = new LinkedHashMap<>();
        String token = jwtUtil.resolve(request.getHeader(HttpHeaders.AUTHORIZATION));

        memberService.blackListToken(token);
        map.put("message", "???????????? ???????????????.");
        return new ResponseEntity<Object>(map, HttpStatus.OK);
    }

    @Operation(summary = "?????? ??????????????? ??????", description = "???????????? ????????????")
    @GetMapping("/info")
    public ResponseEntity<MemberBasicAccountInfoVO> memberinfo(Authentication authentication) {
        Long mbiSeq = Long.parseLong(authentication.getName());

       return new ResponseEntity<>(memberService.memberInfoView(mbiSeq), HttpStatus.OK);         
    }

    @Value("${file.image.background}") String background_img;
    @Autowired BackgroundImgService backgroundImgService;

    @Operation(summary = "?????? ??????", description = "???????????? ??????, ??????????????? FormData?????????")
    @PostMapping(value = "/join")    
    public ResponseEntity<MemberJoinResponse> memberJoin1(@RequestPart(value = "file") @Nullable MultipartFile file, 
    @RequestPart(value = "json") MemberJoinVo data) {
        return new ResponseEntity<>(memberService.memberTotalJoin1(data, file), HttpStatus.OK);
    }    

    @Operation(summary = "?????? ????????? ????????????", description = "?????? ????????? ????????????")
    @PostMapping(value = "/duplicate")    
    public ResponseEntity<MemberDuplicatedResponseVO> memberDuplicated(
    @RequestBody MemberDuplicatedVO data) {
        return new ResponseEntity<>(memberService.memberDuplicatedCheck(data), HttpStatus.OK);
    }     


    // @GetMapping("/test")
    // public ResponseEntity<Object> test(@LoginUserSeq Long mbiSeq) {
    //     Map<String, Object> resultMap = null;
    //     System.out.println(mbiSeq);
    //     return new ResponseEntity<Object>(resultMap, HttpStatus.OK);
    // }

    // @GetMapping("/test2")
    // public String test2(Authentication authentication) {
    //     Long seq = Long.parseLong(authentication.getName());        
    //     return "seq : " + seq;
    // }

    // @GetMapping("/test3")
    // public String test3(@LoginUserSeq Long seq) {
    //     return "seq: " + seq;
    // }
    
    @Operation(summary = "?????? ????????????", description = "???????????? ??????, ??????????????? formdata??????")
    @PostMapping("/change")
    public ResponseEntity<Object> changeMemberInfo(Authentication authentication,@RequestPart(value = "file") @Nullable MultipartFile file, 
    @RequestPart(value = "json") @Nullable MemberInfoChangeVO data){
        Long mbiSeq = Long.parseLong(authentication.getName());
        Map<String, Object> resultMap = memberService.memberInfoChange(mbiSeq, data, file);
        return new ResponseEntity<Object>(resultMap, HttpStatus.OK);
    }

    @Operation(summary = "?????? ?????????", description = "????????? ????????? ????????????(??????????????? ???) ????????? true??? ????????????")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseInfoVO> info1 (@RequestBody LoginMemberVO data) {
        return new ResponseEntity<>(memberService.loginMemberResponse(data),  HttpStatus.OK);
    }

    @Operation(summary = "?????? ???????????? ??????", description = "?????? ???????????? ?????? & ??????????????? ??????")
    @PostMapping("/findpassword")
    public ResponseEntity<FindPwdResponseVO> findByMail (@RequestBody FindPwdVO data) throws Exception {
        return new ResponseEntity<>(memberService.loginUser(data), HttpStatus.OK);
    }
}
