package com.secondproject.coupleaccount.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AWSConfig {
	
	/**
	 * Key는 중요정보이기 때문에 properties 파일에 저장한 뒤 가져와 사용하는 방법이 좋습니다.
	 */
	@Value("${cloud.aws.credentials.accessKey}") String iamAkey;
	@Value("${cloud.aws.credentials.secretKey}") String iamSkey;


	
	private String iamAccessKey = iamAkey; // IAM Access Key
	private String iamSecretKey = iamSkey; // IAM Secret Key

	private String region = "ap-northeast-2"; // Bucket Region 
	
	@Bean
	public AmazonS3Client amazonS3Client() {
		// BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(iamAccessKey, iamSkey);

		BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(iamAkey, iamSkey);
		return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                                                             .withRegion(region)
                                                             .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                                                             .build();
	}
}