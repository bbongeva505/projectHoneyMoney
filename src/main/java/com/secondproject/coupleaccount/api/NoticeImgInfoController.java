package com.secondproject.coupleaccount.api;

import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.secondproject.coupleaccount.entity.NoticeImgInfoEntity;
import com.secondproject.coupleaccount.service.NoticeImgInfoService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeImgInfoController {
@Value("${file.image.notice}") String notice_img;
private final NoticeImgInfoService noticeImgInfoService;
  @PutMapping("/upload")
  public ResponseEntity<Object>putNoticeImg(@RequestPart MultipartFile file){
    Map<String,Object> map = new LinkedHashMap<String,Object>();
    Path folderLocation = Paths.get(notice_img);
    String OriginFileName =file.getOriginalFilename();
    String[]   split = OriginFileName.split("\\.");
    String ext = split[split.length - 1];
    String filename ="";
    for(int i = 0; i < split.length-1; i++){
      filename += split[i];
    }
    String saveFilename = "notice"+"-";
    Calendar c = Calendar.getInstance();
    saveFilename += c.getTimeInMillis()+"."+ext;
    Path targetFile = folderLocation.resolve(saveFilename);
    try{
      Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
        }
    catch(Exception e){
      e.printStackTrace();
    }
    NoticeImgInfoEntity data = new NoticeImgInfoEntity();
    data.setNiiFileName(saveFilename);
    data.setNiiUri(filename);
   noticeImgInfoService.addNoticeImgInfo(data);
    return new ResponseEntity<>(map, HttpStatus.OK);
  }
  @GetMapping("/img/{niiUri}")
  public  ResponseEntity<Resource>getImg(@PathVariable String niiUri, HttpServletRequest request) throws Exception{
    Path folderLocation = Paths.get(notice_img);
    String filename = noticeImgInfoService.getNoticeImgNameUri(niiUri);
    String [] split = filename.split("\\.");
    String ext = split[split.length-1];
    String exportName = niiUri +"."+ ext;
    Path targetFile = folderLocation.resolve(filename);
    Resource r = null;
      try{
      r = new UrlResource(targetFile.toUri());  
      }
      catch(Exception e){
        e.printStackTrace();
      }
      String contentType = null;
      try{
        contentType = request.getServletContext().getMimeType(r.getFile().getAbsolutePath());
        if(contentType == null){
          contentType ="application/octet-stream";
        }
      }
      catch(Exception e){
        e.printStackTrace();
      }
      return ResponseEntity.ok()
      .contentType(MediaType.parseMediaType(contentType))
      .header(HttpHeaders.CONTENT_DISPOSITION,
      "attachment; filename*=\""+URLEncoder.encode(exportName, "UTF-8")+"\"")
      .body(r);
    }

}
