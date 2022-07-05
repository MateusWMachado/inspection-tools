package com.ey.inspectiontools.controller;

import com.ey.inspectiontools.dto.ProfileDTO;
import com.ey.inspectiontools.exception.UsernameNotFoundException;
import com.ey.inspectiontools.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

/** Class that represents a controller to handle information from Users profiles
 *
 * @author Mateus W. Machado
 * @since 24/05/2022
 * @version 1.0.0
 */
@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api/v1")
public class ProfileController {

    private ProfileService profileService;

    @PostMapping("/user/profile/{username}")
    public ResponseEntity<String> createProfileData(@PathVariable String username, @RequestBody ProfileDTO profile, UriComponentsBuilder uriBuilder) throws UsernameNotFoundException {
        return profileService.createProfile(username, profile, uriBuilder);
    }

    @PutMapping("/user/profile/{username}")
    public ResponseEntity<ProfileDTO> updateProfileData(@PathVariable String username, @RequestBody ProfileDTO profile) throws UsernameNotFoundException {
        return profileService.updateProfile(username, profile);
    }

    @GetMapping("/user/profile/{username}")
    public ResponseEntity<ProfileDTO> findProfileData(@PathVariable String username) throws UsernameNotFoundException {
        return profileService.findProfile(username);
    }

}
