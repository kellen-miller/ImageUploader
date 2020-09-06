package com.kellen.ReactAws.service;


import com.kellen.ReactAws.dao.UserProfileDao;
import com.kellen.ReactAws.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserProfileService {

    private final UserProfileDao userProfileDao;

    @Autowired
    public UserProfileService(@Qualifier("postgres") UserProfileDao userProfileDao) {
        this.userProfileDao = userProfileDao;
    }

    public void addUserProfile(UserProfile userProfile) {
        userProfileDao.insertUserProfile(userProfile);
    }

    public Optional<UserProfile> getUserProfileById(UUID id) {
        return userProfileDao.getUserProfileById(id);
    }

    public List<UserProfile> getAllUserProfiles() {
        return userProfileDao.getAllUserProfiles();
    }

    public void uploadUserProfileImage(UUID userProfileId, MultipartFile file) {
        // 1. Check if image is not empty
        // 2. If file is an image
        // 3. The user exists in the db or not
        // 4. Grab some metadata from file if any
        // 5. Store the image in s3 and update database with s3 image link
    }
}
