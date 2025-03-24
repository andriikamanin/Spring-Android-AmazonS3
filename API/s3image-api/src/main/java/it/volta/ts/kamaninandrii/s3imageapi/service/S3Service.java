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
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class S3Service {

    private final AmazonS3 amazonS3;
    private final String bucketName;
    private final AtomicInteger fileCounter;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;


        Dotenv dotenv = Dotenv.load();


        this.bucketName = dotenv.get("AWS_BUCKET_NAME");


        if (bucketName == null) {
            throw new IllegalArgumentException("Missing AWS_S3_BUCKET_NAME environment variable");
        }

        this.fileCounter = new AtomicInteger(1);
    }


    public String uploadFile(MultipartFile multipartFile) {
        File file = convertMultipartFileToFile(multipartFile);

        // Create a file name using the counter
        String fileName = "img_" + fileCounter.getAndIncrement();

        try {
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file));
            return "File uploaded successfully: " + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error uploading file";
        } finally {
            file.delete();
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


    public InputStream getRandomFile() {

        ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request()
                .withBucketName(bucketName);
        ListObjectsV2Result result = amazonS3.listObjectsV2(listObjectsV2Request);


        List<S3ObjectSummary> objectSummaries = result.getObjectSummaries();

        if (objectSummaries.isEmpty()) {
            throw new RuntimeException("No files found in the bucket");
        }


        Random random = new Random();
        S3ObjectSummary randomObjectSummary = objectSummaries.get(random.nextInt(objectSummaries.size()));

        S3Object s3Object = amazonS3.getObject(bucketName, randomObjectSummary.getKey());
        return s3Object.getObjectContent();
    }


    public String getRandomFileName() {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(bucketName);
        ObjectListing objectListing = amazonS3.listObjects(listObjectsRequest);
        List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();

        Random rand = new Random();
        S3ObjectSummary randomObject = objectSummaries.get(rand.nextInt(objectSummaries.size()));

        return randomObject.getKey();
    }
}