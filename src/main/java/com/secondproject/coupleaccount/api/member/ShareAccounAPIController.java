package com.secondproject.coupleaccount.api.member;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.secondproject.coupleaccount.service.ShareAccountInfoService;
import com.secondproject.coupleaccount.vo.ShareAccountJoinVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "데이트 통장(공유통장)", description = "통장 관련 API")

@RestController
@RequestMapping("/api/shareaccount")
public class ShareAccounAPIController {
    @Autowired ShareAccountInfoService shareAccountInfoService;

    @Operation(summary = "공유통장 생성 api", description = "공유통장 생성페이지 api")
    @PostMapping("/join")
    public ResponseEntity<Object> shareAccountJoin(@RequestBody ShareAccountJoinVO data){
        Map<String, Object> resultMap = shareAccountInfoService.addShareAccountInfo(data);
        return new ResponseEntity<Object>(resultMap, HttpStatus.OK);
    }

    @Operation(summary = "데이트통장 체킹 api", description = "데이트통장 중복여부, 기존 보유자 확인, 사용가능 여부 등 체크")
    @GetMapping("/check")
    public ResponseEntity<Object> checkShareAccount(@RequestParam String accountCode){
        Map<String, Object> resultMap = shareAccountInfoService.shareAccountCheck(accountCode);
        return new ResponseEntity<Object>(resultMap, HttpStatus.OK);
    }    
}
