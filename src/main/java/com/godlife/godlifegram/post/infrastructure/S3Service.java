package com.godlife.godlifegram.post.infrastructure;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.godlife.godlifegram.common.exception.ApiErrorException;
import com.godlife.godlifegram.common.response.enums.ResultCode;
import com.godlife.godlifegram.post.infrastructure.dto.ImageDto;
import com.godlife.godlifegram.post.infrastructure.dto.UploadedFileInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public UploadedFileInfoDto upload(MultipartFile file, Long postId) {
        String today = LocalDate.now(ZoneId.of("Asia/Seoul"))
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String originalFilename = file.getOriginalFilename();
        String extension = getExtension(originalFilename);
        String fileName = today + "/" + postId + "/" + UUID.randomUUID() + "." + extension;

        try {
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            BufferedImage resizedImage = resizeIfNeeded(originalImage, 1280);


            byte[] imageBytes = compressImage(resizedImage, extension);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(imageBytes.length);
            metadata.setContentType(getMimeType(extension));
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            amazonS3.putObject(bucket, fileName, inputStream, metadata);

            double sizeInMb = imageBytes.length / 1024.0 / 1024.0;

            return new UploadedFileInfoDto("/post/image/" + fileName, Math.round(sizeInMb * 100.0) / 100.0);
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 실패", e);
        }
    }

    public ImageDto getImage(String s3key) {
        try {
            S3Object object = amazonS3.getObject(bucket, s3key);
            S3ObjectInputStream inputStream = object.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(inputStream);

            String contentType = object.getObjectMetadata().getContentType();
            if (contentType == null) contentType = "image/jpeg";

            return new ImageDto(bytes, contentType);

        } catch (Exception e) {
            throw new ApiErrorException(ResultCode.ERROR);
        }
    }

    public void deleteFile(String s3Key) {
        try {
            if (amazonS3.doesObjectExist(bucket, s3Key)) {
                amazonS3.deleteObject(bucket, s3Key);
            } else {
                throw new ApiErrorException(ResultCode.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new RuntimeException("S3 파일 삭제 실패: " + s3Key, e);
        }
    }


    private BufferedImage resizeIfNeeded(BufferedImage original, int maxWidth) {
        if (original.getWidth() <= maxWidth) return original;

        int newWidth = maxWidth;
        int newHeight = (original.getHeight() * maxWidth) / original.getWidth();

        Image tmp = original.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resized;
    }

    private byte[] compressImage(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(format);
        if (!writers.hasNext()) throw new IllegalStateException("No writer for format: " + format);

        ImageWriter writer = writers.next();
        ImageWriteParam param = writer.getDefaultWriteParam();

        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.8f); // 80% 퀄리티
        }

        writer.setOutput(new MemoryCacheImageOutputStream(output));
        writer.write(null, new IIOImage(image, null, null), param);
        writer.dispose();

        return output.toByteArray();
    }

    private String getExtension(String fileName) {
        String ext = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        return switch (ext) {
            case "jpeg", "jpg" -> "jpeg";
            case "png" -> "png";
            default -> "jpeg"; // 기본 jpeg
        };
    }

    private String getMimeType(String extension) {
        return switch (extension.toLowerCase()) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "webp" -> "image/webp";
            default -> "application/octet-stream";
        };
    }
}
