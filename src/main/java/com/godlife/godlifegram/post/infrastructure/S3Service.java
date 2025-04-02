package com.godlife.godlifegram.post.infrastructure;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile file) {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());

        try {
            amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);
            return amazonS3.getUrl(bucket, fileName).toString();
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 실패", e);
        }
    }

}
