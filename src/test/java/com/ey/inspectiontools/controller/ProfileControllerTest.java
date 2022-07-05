package com.ey.inspectiontools.controller;

import com.ey.inspectiontools.dto.ProfileDTO;
import com.ey.inspectiontools.dto.UserDTO;
import com.ey.inspectiontools.repository.UserRepository;
import com.ey.inspectiontools.service.AuthenticationService;
import com.ey.inspectiontools.service.ChecklistService;
import com.ey.inspectiontools.service.ProfileService;
import com.ey.inspectiontools.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URI;
import java.net.URLEncoder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Profile Controller test class
 *
 * @author Mateus W. Machado
 * @since 31/05/2022
 * @version 1.0.0
 */
@WebMvcTest(ProfileController.class)
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ChecklistService checklistService;

    @MockBean
    private ImageController imageController;

    @MockBean
    private AuthenticationService authenticationService;

    private String json;
    private String username;
    private ProfileDTO profileDTO;

    @BeforeEach
    void mockAUserAndProfileDTOBeforeEachTestSetup() throws JsonProcessingException {
        ProfileDTO profileDTO = ProfileDTO.builder()
                .name("mockName")
                .lastname("mockLastname")
                .email("mock@email.com")
                .build();

        UserDTO userDTO = UserDTO.builder()
                .username("mockUsername")
                .password("mockPassword")
                .role("ADMIN")
                .profile(profileDTO)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        this.json = objectMapper.writeValueAsString(profileDTO);
        this.username = userDTO.getUsername();
        this.profileDTO = profileDTO;
    }
    @Test
    @WithMockUser(username = "mockUser", password = "mockPassword", authorities = "USER")
    @DisplayName("Should execute a POST request on endpoint '/api/v1/user/profile/{username}' with user authorities - USER and call profileService method 'createProfile(username, ProfileDTO.class, UriComponentsBuilder.class)'")
    void createProfileTest() throws Exception {

        Mockito.when(profileService.createProfile(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(ResponseEntity
                        .created(URI.create(URLEncoder.encode("/api/v1/user/")))
                        .body("Created profile"));

        RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/user/profile/{username}", this.username)
                .content(this.json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isCreated());

    }

    @Test
    @WithMockUser(username = "mockUser", password = "mockPassword", authorities = "USER")
    @DisplayName("Should execute a PUT request on endpoint '/api/v1/user/profile/{username}' with user authorities - USER and call profileService method 'updateProfile(username, ProfileDTO.class)'")
    void updateProfileTest() throws Exception {

        Mockito.when(profileService.updateProfile("mockUsername", this.profileDTO)).thenReturn(ResponseEntity.ok(this.profileDTO));

        RequestBuilder request = MockMvcRequestBuilders.put("/api/v1/user/profile/{username}", this.username)
                .content(this.json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isOk());

        Mockito.verify(profileService, Mockito.times(1)).updateProfile(this.username, this.profileDTO);
    }

    @Test
    @WithMockUser(username = "mockUser", password = "mockUser", authorities = "USER")
    @DisplayName("Should execute a GET request on endpoint '/api/v1/user/profile/{username}' with user authorities - USER and call profileService method 'findProfile(username)'")
    void findProfileTest() throws Exception {

        Mockito.when(profileService.findProfile("username")).thenReturn(ResponseEntity.ok(this.profileDTO));

        mockMvc.perform(get("/api/v1/user/profile/{username}", this.username))
                .andExpect(status().isOk());

        Mockito.verify(profileService, Mockito.times(1)).findProfile("mockUsername");

    }
}