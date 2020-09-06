package com.kellen.ReactAws.dao;


import com.kellen.ReactAws.model.UserProfile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileDao {

    void insertUserProfile(UUID id, UserProfile userProfile);

    default void insertUserProfile(UserProfile userProfile) {
        UUID id = UUID.randomUUID();
        insertUserProfile(id, userProfile);
    }

    Optional<UserProfile> getUserProfileById(UUID id);

    List<UserProfile> getAllUserProfiles();
}
