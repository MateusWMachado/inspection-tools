package com.ey.inspectiontools.service;

import com.ey.inspectiontools.dto.ProfileDTO;
import com.ey.inspectiontools.dto.UserDTO;
import com.ey.inspectiontools.dto.UserFormDTO;
import com.ey.inspectiontools.exception.UsernameNotFoundException;
import com.ey.inspectiontools.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/** Profile service test class
 *
 * @author Mateus W. Machado
 * @since 31/05/2022
 * @version 1.0.0
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProfileServiceImplTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    private String usernameDTO;

    private ProfileDTO profileDTO;
    @BeforeEach
    void createAccountAndMockProfileBeforeEachTestSetup() {
        UserDTO userFormDTO = UserDTO.builder()
                .created_at(LocalDate.now())
                .password("MockAccount")
                .role("USER")
                .username("MockAccount")
                .build();

        ProfileDTO mockProfileDTO = ProfileDTO.builder()
                .email("profile@test.com")
                .name("teste")
                .lastname("lastname")
                .build();

        ResponseEntity<String> createdMockAccount = userService.createAccount(userFormDTO, UriComponentsBuilder.newInstance());
        this.usernameDTO = Objects.requireNonNull(createdMockAccount.getBody()).substring(31);
        this.profileDTO = mockProfileDTO;
    }
    @Test
    @DisplayName("Should create the profile, return status code - CREATED and assert all the conditions")
    void shouldCreateProfile() throws UsernameNotFoundException {

        ResponseEntity<String> createdAccount = profileService.createProfile(this.usernameDTO, this.profileDTO, UriComponentsBuilder.newInstance());

        assertNotNull(createdAccount);
        assertEquals(HttpStatus.CREATED, createdAccount.getStatusCode());
        assertEquals("Created profile with name: " + profileDTO.getName(), createdAccount.getBody());
    }

    @Test
    @DisplayName("Should update the profile, return status code - OK and assert all the conditions")
    void updateProfileTest() throws UsernameNotFoundException {

        ProfileDTO profileDTO = ProfileDTO.builder()
                .email("profile@test.com")
                .name("test")
                .lastname("lastname")
                .build();

        ProfileDTO updatedProfileDTO = ProfileDTO.builder()
                .email("profileUpdated@test.com")
                .name("nameUpdated")
                .lastname("lastnameUpdated")
                .build();

        profileService.createProfile(this.usernameDTO, profileDTO, UriComponentsBuilder.newInstance());
        ResponseEntity<ProfileDTO> updatedProfile = profileService.updateProfile(this.usernameDTO, updatedProfileDTO);

        assertNotNull(updatedProfile);
        assertEquals(HttpStatus.OK, updatedProfile.getStatusCode());
        assertEquals(updatedProfileDTO.getEmail(), Objects.requireNonNull(updatedProfile.getBody()).getEmail());
        assertEquals(updatedProfileDTO.getName(), Objects.requireNonNull(updatedProfile.getBody()).getName());
        assertEquals(updatedProfileDTO.getLastname(), Objects.requireNonNull(updatedProfile.getBody()).getLastname());
    }
    @Test
    @DisplayName("Should find a profile by username, return status code - OK and assert all the conditions")
    void findProfileTest() throws UsernameNotFoundException {

        profileService.createProfile(this.usernameDTO, this.profileDTO, UriComponentsBuilder.newInstance());

        ResponseEntity<ProfileDTO> foundedProfile = profileService.findProfile(this.usernameDTO);
        ProfileDTO profileData = foundedProfile.getBody();

        assertNotNull(profileData);
        assertEquals(HttpStatus.OK, foundedProfile.getStatusCode());
        assertEquals(this.profileDTO.getName(), profileData.getName());
        assertEquals(this.profileDTO.getEmail(), profileData.getEmail());
        assertEquals(this.profileDTO.getLastname(), profileData.getLastname());
    }

    @Test
    @DisplayName("Should throw an UsernameNotFoundException if can not find the profile by username")
    void shouldThrowAnExceptionIfCanNotFindTheProfileByUsername() {
        assertThrows(UsernameNotFoundException.class, () -> profileService.findProfile("A1B2C3D4E5F6G7"));
    }

}