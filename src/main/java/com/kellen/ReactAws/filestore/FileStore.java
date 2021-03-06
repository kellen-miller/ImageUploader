package com.kellen.ReactAws.filestore;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class FileStore {

    private final AmazonS3 amazonS3;

    @Autowired
    public FileStore(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public void save(String path, String fileName,
                     Optional<Map<String, String>> optionalMetaData,
                     InputStream inputStream) {
        var metaData = new ObjectMetadata();
        optionalMetaData.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach(metaData::addUserMetadata);
            }
        });

        try {
            amazonS3.putObject(path, fileName, inputStream, metaData);
        } catch (AmazonServiceException ase) {
            throw new IllegalStateException("Failed to store file to S3", ase);
        }
    }

    public byte[] download(String userProfileImageLink) {
        int separator = userProfileImageLink.lastIndexOf("/");
        String path = userProfileImageLink.substring(0, separator);
        String key = userProfileImageLink.substring(separator + 1);
        try {
            return amazonS3.getObject(path, key).getObjectContent().readAllBytes();
        } catch (AmazonServiceException | IOException e) {
            throw new IllegalStateException("Failed to download file from s3");
        }
    }
}
