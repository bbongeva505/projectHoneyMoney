package com.secondproject.coupleaccount.api.aws;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.secondproject.coupleaccount.service.aws.AWS3Download;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AWSDownload {
 
    private final AWS3Download s3Service;
 
    @GetMapping("/api/img/download/{fileName}")
    public ResponseEntity<byte[]> download(@PathVariable String fileName) throws IOException {
        return s3Service.getObject(fileName);
    }
}
