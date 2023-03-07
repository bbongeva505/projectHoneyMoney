package com.secondproject.coupleaccount.api.aws;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.secondproject.coupleaccount.service.aws.AwsS3Service;
import com.secondproject.coupleaccount.utils.CommonUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AWSApi {

	private final AmazonS3Client amazonS3Client;	
	private final AwsS3Service awsS3Service;

	
	private String S3Bucket = "projecthanimoney"; // Bucket 이름
	

	
	// @GetMapping("api/upload")
	// public ResponseEntity<Object> upload(MultipartFile[] multipartFileList) throws Exception {
	// 	List<String> imagePathList = new ArrayList<>();
		
	// 	for(MultipartFile multipartFile: multipartFileList) {
	// 		String originalName = multipartFile.getOriginalFilename(); // 파일 이름
	// 		long size = multipartFile.getSize(); // 파일 크기
			
	// 		ObjectMetadata objectMetaData = new ObjectMetadata();
	// 		objectMetaData.setContentType(multipartFile.getContentType());
	// 		objectMetaData.setContentLength(size);
			
	// 		// S3에 업로드
	// 		amazonS3Client.putObject(
	// 			new PutObjectRequest(S3Bucket, originalName, multipartFile.getInputStream(), objectMetaData)
	// 				.withCannedAcl(CannedAccessControlList.PublicRead)
	// 		);
			
	// 		String imagePath = amazonS3Client.getUrl(S3Bucket, originalName).toString(); // 접근가능한 URL 가져오기
	// 		imagePathList.add(imagePath);
	// 	}
		
	// 	return new ResponseEntity<Object>(imagePathList, HttpStatus.OK);
	// }

	
	@PostMapping("/api/upload1")
	  public String uploadFile(
      @RequestPart(value = "file") MultipartFile multipartFile) throws Exception{
    return awsS3Service.uploadFileV1(multipartFile);
  }

  @GetMapping("/api/download")
  public ResponseEntity<ByteArrayResource> downloadFile (
      @RequestParam("resourcePath") String resourcePath) throws Exception{
    byte[] data = awsS3Service.downloadFileV1(resourcePath);
    ByteArrayResource resource = new ByteArrayResource(data);
    HttpHeaders headers = buildHeaders(resourcePath, data);

    return ResponseEntity
        .ok()
        .headers(headers)
        .body(resource);
  }

  private HttpHeaders buildHeaders(String resourcePath, byte[] data) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentLength(data.length);
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDisposition(CommonUtils.createContentDisposition(resourcePath));
    return headers;
  }

  

  


	


	// @GetMapping("api/get")
	// public
}