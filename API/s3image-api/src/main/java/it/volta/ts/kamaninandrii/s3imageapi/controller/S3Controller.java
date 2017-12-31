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


    @GetMapping("/file/{fileName}")
    public ResponseEntity<Resource> getFileAsResource(@PathVariable String fileName) throws IOException {
        InputStream fileInputStream = s3Service.fetchFileFromS3(fileName);
        ByteArrayResource resource = new ByteArrayResource(fileInputStream.readAllBytes());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileName) // Отображение в браузере
                .contentType(MediaType.IMAGE_JPEG) // Можно динамически определять MIME-тип
                .contentLength(resource.contentLength())
                .body(resource);
    }



    @GetMapping("/random-image")
    public ResponseEntity<Resource> getRandomImageFromS3() throws IOException {
        // Получаем случайное имя файла
        String randomFileName = s3Service.getRandomFileNameFromS3();

        // Загружаем файл из S3
        InputStream fileInputStream = s3Service.fetchFileFromS3(randomFileName);
        System.out.println("Trying to fetch random file: " + randomFileName);

        // Указываем MIME-тип для изображения
        String contentType = "image/jpeg";  // Здесь можно сделать более гибким, определяя формат изображения

        // Читаем данные из InputStream в ресурс
        ByteArrayResource resource = new ByteArrayResource(fileInputStream.readAllBytes());

        // Возвращаем ресурс как часть ответа
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(resource.contentLength())
                .body(resource);
    }
}