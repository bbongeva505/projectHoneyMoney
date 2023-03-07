package com.secondproject.coupleaccount.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.secondproject.coupleaccount.entity.AccountBookImgEntity;
import com.secondproject.coupleaccount.repository.AccountBookImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountBookImgService {
    private final AccountBookImgRepository accountBookImgRepository;
    public Map<String,Object> addAccountBookImgInfo(AccountBookImgEntity data){
        Map<String,Object> map = new LinkedHashMap<String,Object>();
        accountBookImgRepository.save(data);
        return map;
    }

    public AccountBookImgEntity getAccountBookImgNameUri(String aiUri) {
        // AccountBookImgEntity data = accountBookImgRepository.findByAiImgName(aiUri);
        return accountBookImgRepository.findByAiImgName(aiUri);
        // return data.getAiImgName();
    }
    
   
}
