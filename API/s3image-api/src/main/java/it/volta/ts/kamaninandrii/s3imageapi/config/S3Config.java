package it.volta.ts.kamaninandrii.s3imageapi.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Bean
    public AmazonS3 amazonS3() {
        // Загружаем .env файл
        Dotenv dotenv = Dotenv.load();

        // Получаем переменные окружения
        String awsAccessKeyId = dotenv.get("AWS_ACCESS_KEY_ID");
        String awsSecretAccessKey = dotenv.get("AWS_SECRET_ACCESS_KEY");
        String awsRegion = dotenv.get("AWS_REGION");

        // Проверяем, что все переменные окружения получены
        if (awsAccessKeyId == null || awsSecretAccessKey == null || awsRegion == null) {
            throw new IllegalArgumentException("Missing one or more AWS environment variables: AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY, AWS_REGION");
        }

        // Создаем BasicAWSCredentials с данными из .env
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);

        // Строим клиент AmazonS3 с использованием переменных окружения
        return AmazonS3ClientBuilder.standard()
                .withRegion(awsRegion)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}