package uz.ccrew.assignmentservice.file.service.impl;

import uz.ccrew.assignmentservice.file.service.StorageService;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;

@Service
public class StorageServiceImpl implements StorageService {
    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    private final AmazonS3 amazonS3;

    public StorageServiceImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public void upload(String fileOriginalName, InputStream inputStream, ObjectMetadata metadata) {
        amazonS3.putObject(bucketName, fileOriginalName, inputStream, metadata);
    }
}