package com.ey.inspectiontools.service;

import com.ey.inspectiontools.dto.ProfileDTO;
import com.ey.inspectiontools.exception.UsernameNotFoundException;
import com.ey.inspectiontools.model.Profile;
import com.ey.inspectiontools.model.User;
import com.ey.inspectiontools.repository.ProfileRepository;
import com.ey.inspectiontools.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/** ProfileService contract implementation class
 *
 * @author Mateus W. Machado
 * @since 24/05/2022
 * @version 1.0.0
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
public class ProfileServiceImpl implements ProfileService{
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private ProfileRepository profileRepository;

    /** Method to create a User Profile
     *
     * @author Mateus W. Machado
     * @param username the username to find the user
     * @param profileDTO the profile form
     * @param uriBuilder used for constructing URI
     * @return if the method finds a user with that username it must create the profile for that user, return a HttpStatus - CREATED and a string with a message
     * if the method does not found a user with that username it must return a UsernameNotFoundException
     */
    @Override
    public ResponseEntity<String> createProfile(String username, ProfileDTO profileDTO, UriComponentsBuilder uriBuilder) throws UsernameNotFoundException {
        User byUsername = verifyIfUsernameExist(username);
        byUsername.setProfile(modelMapper.map(profileDTO, Profile.class));
        User savedUser = userRepository.save(modelMapper.map(byUsername, User.class));

        URI uri = uriBuilder.path("/api/v1/user/profile/{username}").buildAndExpand(savedUser.getUsername()).toUri();
        return ResponseEntity.created(uri).body("Created profile with name: " + profileDTO.getName());
    }

    /** Method to update a User Profile
     *
     * @author Mateus W. Machado
     * @param username the username to find the user
     * @param profile the updated profile form
     * @return if the method finds a user with that username it must update the profile for that user and return a HttpStatus - OK
     * if the method does not found a user with that username it must return a UsernameNotFoundException
     */
    @Override
    public ResponseEntity<ProfileDTO> updateProfile(String username, ProfileDTO profile) throws UsernameNotFoundException {
        User byUsername = verifyIfUsernameExist(username);
        return profileRepository.findById(byUsername.getProfile().getId()).map(newProfile -> {
            newProfile.setEmail(profile.getEmail());
            newProfile.setName(profile.getName());
            newProfile.setLastname(profile.getLastname());
            Profile savedProfile = profileRepository.save(newProfile);
            return ResponseEntity.ok(modelMapper.map(savedProfile, ProfileDTO.class));
        }).orElse(ResponseEntity.notFound().build());
    }

    /** Method to find a profile by username
     *
     * @author Mateus W. Machado
     * @param username the username to find the user
     * @return if the method finds a user with that username it must return the ProfileDTO from that User and a HttpStatus - OK
     */
    @Override
    public ResponseEntity<ProfileDTO> findProfile(String username) throws UsernameNotFoundException {
        User user = verifyIfUsernameExist(username);
        ProfileDTO profileDTO = modelMapper.map(user.getProfile(), ProfileDTO.class);
        return ResponseEntity.ok(profileDTO);
    }

    /** Method to verify if the user exists
     *
     * @author Mateus W. Machado
     * @param username the username to find the user
     * @return if the method finds a user with that username it must return the User, if the method does not found it must return a UsernameNotFoundException
     */
    private User verifyIfUsernameExist(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
