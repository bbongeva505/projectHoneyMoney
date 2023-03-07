package com.secondproject.coupleaccount.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.secondproject.coupleaccount.entity.BackgroundImgInfoEntity;
import com.secondproject.coupleaccount.repository.BackgroundImgInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BackgroundImgService {
    private final BackgroundImgInfoRepository backgroundImgInfoRepository;
    public Map<String,Object> addBackgroundImage(BackgroundImgInfoEntity data){
        Map<String,Object> map = new LinkedHashMap<String,Object>();
        backgroundImgInfoRepository.save(data);
        return map;
    }
    public String getBackgroundimgNameUri(String biiUri){
        List<BackgroundImgInfoEntity>data = backgroundImgInfoRepository.findByBiiUri(biiUri);
        return data.get(0).getBiiFileName();
    }
}
