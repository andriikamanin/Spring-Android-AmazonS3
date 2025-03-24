package it.volta.ts.kamaninandrii.s3imageapi.controller;

import it.volta.ts.kamaninandrii.s3imageapi.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.model.S3Object;

import java.io.IOException;
import java.io.InputStream;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/s3")
public class S3Controller {

    @Autowired
    private S3Service s3Service;


    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return s3Service.uploadFile(file);
    }


    @GetMapping("/file-url/{fileName}")
    public ResponseEntity<Resource> getFileUrl(@PathVariable String fileName) throws IOException {
        InputStream fileInputStream = s3Service.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(fileInputStream.readAllBytes());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileName) // Изменено на "inline"
                .contentType(MediaType.IMAGE_JPEG) // Укажите правильный MIME-тип, если изображения могут быть PNG или другие форматы, можно определить динамически
                .contentLength(resource.contentLength())
                .body(resource);
    }


    @GetMapping("/random-file")
    public ResponseEntity<Resource> getRandomFile() throws IOException {
        String randomFileName = s3Service.getRandomFileName();
        InputStream fileInputStream = s3Service.downloadFile(randomFileName);
        System.out.println("Trying to download file: " + randomFileName);
        String contentType = "image/jpeg";

        ByteArrayResource resource = new ByteArrayResource(fileInputStream.readAllBytes());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(resource.contentLength())
                .body(resource);
    }
}