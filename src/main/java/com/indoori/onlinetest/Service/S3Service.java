package com.indoori.onlinetest.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class S3Service {
    @Autowired
    AmazonS3 amazonS3;
    @Value("${s3.buckek.name}")
    String defaultBucketName;

    @Value("${s3.default.folder}")
    String defaultBaseFolder;

    public List<Bucket> getAllBuckets() {
        return amazonS3.listBuckets();
    }

    public byte[] getFile(String key) {
        S3Object obj = amazonS3.getObject(defaultBucketName, defaultBaseFolder+"/"+key);
        S3ObjectInputStream stream = obj.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(stream);
            obj.close();
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
