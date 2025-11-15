/*
package com.tcon.empManagement.config;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class EmployeeDocumentConfig {

    @Value("${gcs.bucket.name}")
    private String bucketName;

    @Value("${gcs.bucket.serviceAccount.filename}")
    private String serviceAccountFileName;

    @Bean("serviceAccountStorage")
    public Storage serviceAccountStorage(Storage defaultStorage) throws IOException {
        Blob blob = defaultStorage.get(BlobId.of(bucketName, serviceAccountFileName));
        byte[] content = blob.getContent();
        InputStream credentialsStream = new ByteArrayInputStream(content);
        return StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(credentialsStream))
                .build()
                .getService();
    }

    @Bean
    @Primary
    public Storage defaultStorage() {
        return StorageOptions.getDefaultInstance().getService();
    }
}
*/
