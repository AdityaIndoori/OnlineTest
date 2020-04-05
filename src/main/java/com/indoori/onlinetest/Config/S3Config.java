package com.indoori.onlinetest.Config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Tutorial URL: https://blogs.ashrithgn.com/aws-s3-with-spring-boot-uploading-and-downloading-file-to-buckets/
@Configuration
public class S3Config {
    @Value("${s3.access.key}")
    String accessKey;
    @Value("${s3.access.secret}")
    String accessSecret;

    @Bean
    public AmazonS3 generateS3Client() {
        BasicAWSCredentials creds = new BasicAWSCredentials(accessKey, accessSecret);
        return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).build();
    }
}
