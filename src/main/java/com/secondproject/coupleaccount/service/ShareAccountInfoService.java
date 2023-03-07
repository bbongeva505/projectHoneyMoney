package com.secondproject.coupleaccount.service;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.secondproject.coupleaccount.entity.MemberBasicInfoEntity;
import com.secondproject.coupleaccount.entity.ShareAccountInfoEntity;
import com.secondproject.coupleaccount.repository.MemberBasicInfoRepository;
import com.secondproject.coupleaccount.repository.ShareAccountInfoRepository;
import com.secondproject.coupleaccount.vo.ShareAccountJoinVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ShareAccountInfoService {
    @Autowired
    ShareAccountInfoRepository sRepo; 
    @Autowired
    MemberBasicInfoRepository mRepo;   

    public String randomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
  
    public Map<String, Object> addShareAccountInfo(ShareAccountJoinVO data) {
      
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        String accountCode = randomString();      

        ShareAccountInfoEntity entity = ShareAccountInfoEntity.builder().saiName(data.getName())
                            .saiStartDay(data.getStartDay())
                            .saiCode(accountCode).build();       

        sRepo.save(entity);

        map.put("status", true);
        map.put("accountCode", accountCode);
        map.put("message", "공유통장정보가 저장되었습니다.");
        map.put("code", HttpStatus.OK);        
        
        return map;        
    }

    public Map<String, Object> shareAccountCheck(String accountCode){
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        ShareAccountInfoEntity shareAccount = sRepo.findBySaiCode(accountCode);

        if(shareAccount == null) {
            map.put("result", false);
            map.put("message", "코드에 해당하는 데이트 통장이 없습니다");
            map.put("code", HttpStatus.BAD_REQUEST);
            return map;
        }     

        else if(mRepo.findByShareAccount(shareAccount).size() == 2) {
            map.put("result", false);
            map.put("message", "이미 사용중인 데이트 통장입니다");
            map.put("code", HttpStatus.BAD_REQUEST);
            return map;
        }

        else if(mRepo.findByShareAccount(shareAccount).size() == 0) {
            map.put("result", true);
            map.put("message", "사용가능한 데이트 통장입니다");
            map.put("cde", HttpStatus.OK);
            return map;
        }

            List<MemberBasicInfoEntity> memberBasicInfoEntity = mRepo.findByShareAccount(shareAccount);
            map.put("result", true);
            map.put("message", memberBasicInfoEntity.get(0).getMbiName()+"님의 통장");
            map.put("code", HttpStatus.OK);            
       
        return map;
    }


   
}
