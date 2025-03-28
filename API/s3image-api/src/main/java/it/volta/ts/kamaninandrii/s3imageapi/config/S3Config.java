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

        Dotenv dotenv = Dotenv.load();


        String awsAccessKeyId = dotenv.get("AWS_ACCESS_KEY_ID");
        String awsSecretAccessKey = dotenv.get("AWS_SECRET_ACCESS_KEY");
        String awsRegion = dotenv.get("AWS_REGION");


        if (awsAccessKeyId == null || awsSecretAccessKey == null || awsRegion == null) {
            throw new IllegalArgumentException("Missing one or more AWS environment variables: AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY, AWS_REGION");
        }


        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);


        return AmazonS3ClientBuilder.standard()
                .withRegion(awsRegion)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}