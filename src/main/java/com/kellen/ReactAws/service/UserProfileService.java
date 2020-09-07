package com.kellen.ReactAws.service;


import com.kellen.ReactAws.bucket.BucketName;
import com.kellen.ReactAws.dao.UserProfileDao;
import com.kellen.ReactAws.filestore.FileStore;
import com.kellen.ReactAws.model.UserProfile;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.util.Map.entry;

@Service
public class UserProfileService {

    private final UserProfileDao userProfileDao;
    private final FileStore fileStore;

    @Autowired
    public UserProfileService(@Qualifier("postgres") UserProfileDao userProfileDao, FileStore fileStore) {
        this.userProfileDao = userProfileDao;
        this.fileStore = fileStore;
    }

    public List<UserProfile> getAllUserProfiles() {
        return userProfileDao.getAllUserProfiles();
    }

    public Optional<UserProfile> getUserProfileById(UUID id) {
        return userProfileDao.getUserProfileById(id);
    }

    public void addUserProfile(UserProfile userProfile) {
        userProfileDao.insertUserProfile(userProfile);
    }

    public void uploadUserProfileImage(UserProfile userProfile, MultipartFile file) {
        isFileEmpty(file);
        isFileImage(file);
        doesUserExist(userProfile.getUserProfileId());
        var path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), userProfile.getUserProfileId());
        var fileName = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
        var metadata = extractMetadata(file);
        try {
            fileStore.save(path, fileName, Optional.of(metadata), file.getInputStream());
            userProfile.setUserProfileImageLink(path + "/" + fileName);
            userProfileDao.updateUserProfile(userProfile);
        } catch (IOException ioe) {
            throw new IllegalStateException(ioe);
        }
    }

    public byte[] downloadUserProfileImage(UUID userProfileId) {
        UserProfile userProfile = getUserProfileById(userProfileId)
                .orElseThrow(() -> new IllegalStateException("User does not exist"));
        return userProfile.getUserProfileImageLink()
                .map(fileStore::download)
                .orElse(new byte[0]);
    }

    @SuppressWarnings("ConstantConditions")
    private Map<String, String> extractMetadata(MultipartFile file) {
        return Map.ofEntries(
                entry("Content-Type", file.getContentType()),
                entry("Content-Length", String.valueOf(file.getSize()))
        );
    }

    private void doesUserExist(UUID userProfileId) {
        if (getUserProfileById(userProfileId).isEmpty()) throw new IllegalStateException("User cannot be found");
    }

    private void isFileImage(MultipartFile file) {
        try {
            if (!new Tika().detect(file.getBytes()).startsWith("image")) {
                throw new IllegalStateException("File is not an image [" + file.getContentType() + "]");
            }
        } catch (IOException ioe) {
            throw new IllegalStateException("File is corrupted");
        }

    }

    private void isFileEmpty(MultipartFile file) {
        if (file.isEmpty()) throw new IllegalStateException("Cannot upload empty file");
    }
}