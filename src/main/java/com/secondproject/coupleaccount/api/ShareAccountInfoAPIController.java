package com.secondproject.coupleaccount.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.secondproject.coupleaccount.service.ShareAccountInfoService;
import com.secondproject.coupleaccount.vo.ShareAccountJoinVO;



@RestController
@RequestMapping("/api/shareaccount")

public class ShareAccountInfoAPIController {
    @Autowired
    ShareAccountInfoService sService;
    @PostMapping("/add")
    public ResponseEntity<Object> AddAccountInfo(@RequestBody ShareAccountJoinVO data) {
        Map<String, Object> resultMap = sService.addShareAccountInfo(data);
        return new ResponseEntity<Object>(resultMap, (HttpStatus) resultMap.get("code"));
    }    
}
