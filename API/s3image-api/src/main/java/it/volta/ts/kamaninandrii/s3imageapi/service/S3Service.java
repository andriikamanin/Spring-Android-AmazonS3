package it.volta.ts.kamaninandrii.s3imageapi.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class S3Service {

    private final AmazonS3 amazonS3;
    private final String bucketName;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;

        // Загружаем .env файл
        Dotenv dotenv = Dotenv.load();

        // Получаем имя S3 бакета из .env
        this.bucketName = dotenv.get("AWS_BUCKET_NAME");

        // Проверяем, что переменная окружения с именем бакета получена
        if (bucketName == null) {
            throw new IllegalArgumentException("Missing AWS_S3_BUCKET_NAME environment variable");
        }
    }

    // Upload file to S3 bucket
    public String uploadFile(MultipartFile multipartFile) {
        File file = convertMultipartFileToFile(multipartFile);
        String fileName = multipartFile.getOriginalFilename();

        try {
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file));
            return "File uploaded successfully: " + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error uploading file";
        } finally {
            file.delete(); // Удаляем временный файл после загрузки
        }
    }

    // Download file from S3 bucket
    public InputStream downloadFile(String fileName) {
        S3Object s3Object = amazonS3.getObject(bucketName, fileName);
        return s3Object.getObjectContent();
    }

    // Convert MultipartFile to File
    private File convertMultipartFileToFile(MultipartFile multipartFile) {
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error converting MultipartFile to File", e);
        }
        return file;
    }
}