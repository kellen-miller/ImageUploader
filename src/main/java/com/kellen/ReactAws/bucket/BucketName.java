package com.kellen.ReactAws.bucket;

public enum BucketName {

    PROFILE_IMAGE("kellen-image-upload");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
