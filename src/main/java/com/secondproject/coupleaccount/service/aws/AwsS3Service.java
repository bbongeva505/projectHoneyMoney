package com.secondproject.coupleaccount.service.aws;


import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.secondproject.coupleaccount.utils.CommonUtils;

import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AwsS3Service {

  private final AmazonS3Client amazonS3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;

  public String uploadFileV1(MultipartFile multipartFile) throws Exception {
    validateFileExists(multipartFile);

    String fileName = CommonUtils.buildFileName(multipartFile.getOriginalFilename());

    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType(multipartFile.getContentType());

    try (InputStream inputStream = multipartFile.getInputStream()) {
        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
            .withCannedAcl(CannedAccessControlList.PublicRead));
      } catch (IOException e) {
        throw null;
      }

    return amazonS3Client.getUrl(bucketName, fileName).toString();
  }

	private void validateFileExists(MultipartFile multipartFile) {
    if (multipartFile.isEmpty()) {
      throw null;
    }
  }


  public byte[] downloadFileV1(String resourcePath) throws Exception {
    validateFileExistsAtUrl(resourcePath);

    S3Object s3Object = amazonS3Client.getObject(bucketName, resourcePath);
    S3ObjectInputStream inputStream = s3Object.getObjectContent();
    // try {
      return IOUtils.toByteArray(inputStream);
    // } catch (IOException e) {
    //   throw null;
    // }
  }

	private void validateFileExistsAtUrl(String resourcePath) {
    if (!amazonS3Client.doesObjectExist(bucketName, resourcePath)) {
      throw null;
    }
  }
}