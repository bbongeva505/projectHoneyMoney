package com.secondproject.coupleaccount.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.LinkedHashMap;

import java.util.Map;


import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;


import com.secondproject.coupleaccount.entity.NoticeImgInfoEntity;

import com.secondproject.coupleaccount.repository.NoticeImgInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeImgInfoService {
  @Value("${file.image.notice}") String notice_img;
  // private final  NoticeImgInfoRepository noticeImgInfoRepository;
  
  private final NoticeImgInfoRepository noticeImgInfoRepository;

  public Map<String,Object> addNoticeImgInfo(NoticeImgInfoEntity data){
    Map<String,Object>map = new LinkedHashMap<String,Object>();
  noticeImgInfoRepository.save(data);
    return map;
}

  public String getNoticeImgNameUri(String niiUri){
    NoticeImgInfoEntity data = noticeImgInfoRepository.findByNiiUri(niiUri);
    return data.getNiiFileName();

  // public Map<String,Object> noticeAdd(@RequestPart(value = "file") MultipartFile file,
  // @RequestPart(value = "json") NoticeInfoEntity data) {
  //     Map<String, Object> resultMap = noticeInfoService.noticeInfoAdd(data);
  //     Path folderLocation = Paths.get(notice_img);
  //     String originFileName = file.getOriginalFilename();
  //     String [] split  = originFileName.split("\\.");
  //     String ext = split[split.length-1];
  //     String filename = "";
  //     for(int i= 0; i < split.length-1; i++){
  //         filename += split[i];
  //     }
  //     String saveFileName = "notice"+"-";
  //     Calendar c = Calendar.getInstance();
  //     saveFileName += c.getTimeInMillis()+"."+ext;
  //     Path targetFile = folderLocation.resolve(saveFileName);
  //     try{
  //         Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
  //     }
  //     catch(Exception e){
  //         e.printStackTrace();
  //     }
  //     NoticeImgInfoEntity data1 = new NoticeImgInfoEntity();
  //     data1.setNiiFileName(saveFileName);
  //     data1.setNiiUri(filename);
  //     NoticeImgInfoService.addNoticeImgInfo(data1);
  //     return resultMap;
    
  }
}

