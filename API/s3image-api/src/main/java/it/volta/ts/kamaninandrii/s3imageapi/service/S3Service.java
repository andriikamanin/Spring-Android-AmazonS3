package it.volta.ts.kamaninandrii.s3imageapi.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class S3Service {

    private final AmazonS3 amazonS3;
    private final String bucketName;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;

        Dotenv dotenv = Dotenv.load();
        this.bucketName = dotenv.get("AWS_BUCKET_NAME");

        if (bucketName == null) {
            throw new IllegalArgumentException("Missing AWS_S3_BUCKET_NAME environment variable");
        }
    }

    public String uploadFile(MultipartFile multipartFile) {
        File file = convertMultipartFileToFile(multipartFile);

        // Сохраняем расширение файла (если есть)
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";

        // Генерируем уникальное имя файла
        String fileName = "img_" + UUID.randomUUID() + extension;

        try {
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file));
            return "File uploaded successfully: " + getFileUrl(fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error uploading file";
        } finally {
            file.delete();
        }
    }

    public String getFileUrl(String fileName) {
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    public String getRandomFileUrl() {
        ListObjectsV2Result result = amazonS3.listObjectsV2(bucketName);
        List<S3ObjectSummary> objectSummaries = result.getObjectSummaries();

        if (objectSummaries.isEmpty()) {
            throw new RuntimeException("No files found in the bucket");
        }

        Random random = new Random();
        String randomFileName = objectSummaries.get(random.nextInt(objectSummaries.size())).getKey();

        return getFileUrl(randomFileName);
    }

    public InputStream fetchFileFromS3(String fileName) {
        try {
            S3Object s3Object = amazonS3.getObject(bucketName, fileName);
            return s3Object.getObjectContent();
        } catch (AmazonS3Exception e) {
            throw new RuntimeException("Ошибка загрузки файла: " + fileName, e);
        }
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) {
        try {
            File file = File.createTempFile("upload", null);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(multipartFile.getBytes());
            }
            return file;
        } catch (IOException e) {
            throw new RuntimeException("Error converting MultipartFile to File", e);
        }
    }

    public String getRandomFileNameFromS3() {
        ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request()
                .withBucketName(bucketName);
        ListObjectsV2Result result = amazonS3.listObjectsV2(listObjectsV2Request);

        List<S3ObjectSummary> objectSummaries = result.getObjectSummaries();

        if (objectSummaries.isEmpty()) {
            throw new RuntimeException("No files found in the bucket");
        }

        // Получаем случайный объект из списка
        Random random = new Random();
        S3ObjectSummary randomObjectSummary = objectSummaries.get(random.nextInt(objectSummaries.size()));
        return randomObjectSummary.getKey();
    }
}