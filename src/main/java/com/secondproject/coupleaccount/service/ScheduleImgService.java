package com.secondproject.coupleaccount.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import org.springframework.stereotype.Service;


import com.secondproject.coupleaccount.entity.ScheduleImgInfoEntity;

import com.secondproject.coupleaccount.repository.SchduleImgInfoRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleImgService {
    private final SchduleImgInfoRepository schdulimg;
    public Map<String,Object> scheulIngInfo(ScheduleImgInfoEntity data){
        Map<String,Object> map = new LinkedHashMap<String,Object>();
        schdulimg.save(data);
        return map;
    }

    public String getSchedulNameUri(String siiUri) {
        List<ScheduleImgInfoEntity> data = schdulimg.findBySiiUri(siiUri);
        return data.get(0).getSiiFileName();
    }   

}


