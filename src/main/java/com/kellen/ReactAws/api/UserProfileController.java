package com.kellen.ReactAws.api;

import com.kellen.ReactAws.model.UserProfile;
import com.kellen.ReactAws.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/user-profile")
@CrossOrigin("*")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public List<UserProfile> getUserProfiles() {
        return userProfileService.getAllUserProfiles();
    }

    @GetMapping(path = "{id}")
    public UserProfile getUserProfileById(@PathVariable("id") UUID id) {
        return userProfileService.getUserProfileById(id).orElse(null);
    }

    @PostMapping
    public void addUserProfile(@Valid @RequestBody UserProfile userProfile) {
        userProfileService.addUserProfile(userProfile);
    }

    @PostMapping(
            path = "{userProfileId}/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void uploadUserProfileImage(@PathVariable("userProfileId") UUID userProfileId,
                                       @RequestParam("file") MultipartFile file) {
        var userProfile = userProfileService.getUserProfileById(userProfileId);
        if (userProfile.isEmpty()) throw new IllegalStateException("Unable to find specified user");
        userProfileService.uploadUserProfileImage(userProfile.get(), file);
    }

    @GetMapping("{userProfileId}/image/download")
    public byte[] downloadUserProfileImage(@PathVariable("userProfileId") UUID userProfileId) {
        return userProfileService.downloadUserProfileImage(userProfileId);
    }

}
