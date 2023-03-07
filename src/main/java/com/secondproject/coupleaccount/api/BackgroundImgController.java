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

import com.secondproject.coupleaccount.entity.BackgroundImgInfoEntity;
import com.secondproject.coupleaccount.service.BackgroundImgService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
    @RequestMapping("api/background")
    @RequiredArgsConstructor
public class BackgroundImgController {
    @Value("${file.image.background}") String background_img;
private final BackgroundImgService backgroundImgService;
    
    @PutMapping("/upload")
    public ResponseEntity<Object> putBackgroundImg(@RequestPart MultipartFile file, Long memberNo ){
        Map<String,Object> map = new LinkedHashMap<String,Object>();
        Path folderLocation = Paths.get(background_img);
        String originFileName = file.getOriginalFilename();
        String [] split  = originFileName.split("\\.");
        String ext = split[split.length-1];
        String filename = "";
        for(int i= 0; i < split.length-1; i++){
            filename += split[i];
        }
        String saveFileName = "background"+"-";
        Calendar c = Calendar.getInstance();
        saveFileName += c.getTimeInMillis()+"."+ext;
        Path targetFile = folderLocation.resolve(saveFileName);
        try{
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        BackgroundImgInfoEntity data = new BackgroundImgInfoEntity();
        data.setBiiFileName(saveFileName);
        data.setBiiUri(filename);
        data.setBiiMbiSeq(memberNo);
        // data.setBiiUri("http://localhost:9090/background/img/"+filename);
        backgroundImgService.addBackgroundImage(data);
        return new ResponseEntity<>(map,HttpStatus.OK);
       }
       @GetMapping("/img/{biiUri}")
       public ResponseEntity<Resource> getBackgroundImg(@PathVariable String biiUri, HttpServletRequest request)throws Exception{
        Path folderLocation = Paths.get(background_img);
        String filename = backgroundImgService.getBackgroundimgNameUri(biiUri);
        String [] split = filename.split("\\.");
        String ext = split[split.length-1];
        String exportName = biiUri+"."+ext;
        Path targetFile = folderLocation.resolve(filename);
        Resource r = null;
        try{
            r = new UrlResource(targetFile.toUri());
        }
        catch(Exception e){
            e.printStackTrace();
        }
        String contentTyep = null;
        try{
            contentTyep = request.getServletContext().getMimeType(r.getFile().getAbsolutePath());
            if(contentTyep == null){
                contentTyep = "application/octet=stream";
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentTyep))
        .header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename*=\"" + URLEncoder.encode(exportName, "UTF-8")+"\"")
        .body(r);
       }
}
