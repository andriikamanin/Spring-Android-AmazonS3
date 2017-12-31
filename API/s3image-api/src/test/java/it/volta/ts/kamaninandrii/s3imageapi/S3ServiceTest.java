package it.volta.ts.kamaninandrii.s3imageapi;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import it.volta.ts.kamaninandrii.s3imageapi.service.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class S3ServiceTest {

    @Mock
    private AmazonS3 amazonS3;

    @InjectMocks
    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadFile() {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("image.jpg");

        // Mock the behavior of S3 upload
        String fileName = "img_123456";
        doNothing().when(amazonS3).putObject(any(PutObjectRequest.class));

        String response = s3Service.uploadFile(multipartFile);

        assertEquals("File uploaded successfully: " + fileName, response);
    }

    @Test
    void getRandomFileUrl() {
        List<S3ObjectSummary> mockFileList = mock(List.class);
        S3ObjectSummary mockObject = mock(S3ObjectSummary.class);
        when(mockObject.getKey()).thenReturn("random-image.jpg");
        when(mockFileList.isEmpty()).thenReturn(false);
        when(mockFileList.get(anyInt())).thenReturn(mockObject);

        // Mock behavior of S3 object listing
        ListObjectsV2Result mockResult = mock(ListObjectsV2Result.class);
        when(mockResult.getObjectSummaries()).thenReturn(mockFileList);
        when(amazonS3.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(mockResult);

        String fileUrl = s3Service.getRandomFileUrl();
        assertEquals("https://mock-bucket.s3.amazonaws.com/random-image.jpg", fileUrl);
    }

    @Test
    void fetchFileFromS3() {
        S3Object mockS3Object = mock(S3Object.class);
        InputStream mockInputStream = mock(InputStream.class);
        when(mockS3Object.getObjectContent()).thenReturn(mockInputStream);
        when(amazonS3.getObject(anyString(), anyString())).thenReturn(mockS3Object);

        InputStream inputStream = s3Service.fetchFileFromS3("image.jpg");
        assertNotNull(inputStream);
    }
}