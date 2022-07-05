package com.ey.inspectiontools.controller;

import com.ey.inspectiontools.dto.UserDTO;
import com.ey.inspectiontools.dto.checklistDTO.ChecklistDTO;
import com.ey.inspectiontools.repository.UserRepository;
import com.ey.inspectiontools.service.AuthenticationService;
import com.ey.inspectiontools.service.ChecklistService;
import com.ey.inspectiontools.service.ProfileService;
import com.ey.inspectiontools.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Checklist Controller test class
 *
 * @author Mateus W. Machado
 * @since 31/05/2022
 * @version 1.0.0
 */
@WebMvcTest(ChecklistController.class)
class ChecklistControllerTest {

    @MockBean
    private ChecklistService checklistService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ImageController imageController;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    @WithMockUser(username = "mockUser", password = "mockPassword", authorities = "USER")
    @DisplayName("Should execute a GET request on endpoint '/api/v1/user/checklist' with user authorities - USER and call checklistService method 'findAllChecklists()'")
    void findAllChecklistsTest() throws Exception {

        mockMvc.perform(get("/api/v1/user/checklist")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

        Mockito.verify(checklistService, Mockito.times(1)).findAllChecklists();


    }
    @Test
    @WithMockUser(username = "mockUser", password = "mockPassword", authorities = "USER")
    @DisplayName("Should execute a GET request on endpoint '/api/v1/user/checklist/{id}' with user authorities - USER and call checklistService method 'findChecklistById(ID)'")
    void findChecklistByIdTest() throws Exception {

        ChecklistDTO checklistDTO = ChecklistDTO.builder().build();

        Mockito.when(checklistService.findChecklistById(1L)).thenReturn(ResponseEntity.ok(checklistDTO));

        mockMvc.perform(get("/api/v1/user/checklist/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

        Mockito.verify(checklistService, Mockito.times(1)).findChecklistById(1L);

    }

    @Test
    @WithMockUser(username = "mockUser", password = "mockPassword", authorities = "USER")
    @DisplayName("Should execute a POST request on endpoint '/api/v1/user/create-checklist/{username}' with user authorities - USER and call checklistService method 'createChecklist(ChecklistDTO.class, username, UriComponentsBuilder.class)'")
    void createChecklistTest() throws Exception {

        UserDTO userDTO = UserDTO.builder()
                .username("mockAdmin")
                .password("mockPassword")
                .role("ADMIN")
                .build();

        ChecklistDTO checklistDTO = ChecklistDTO.builder()
                .user(userDTO)
                .build();

        Mockito.when(checklistService.createChecklist(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(ResponseEntity
                        .created(URI.create(URLEncoder.encode("/api/v1/user/create-checklist/{id}")))
                        .body("Created checklist"));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(checklistDTO);

        RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/user/create-checklist/{username}", userDTO.getUsername())
                .accept(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(json)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().string("Created checklist"));
    }

    @Test
    @WithMockUser(username = "mockUser", password = "mockPassword", authorities = "USER")
    @DisplayName("Should execute a DELETE request on endpoint '/api/v1/user/checklist/{id}' with user authorities - USER and call checklistService method 'deleteChecklist(ID)'")
    void deleteChecklistTest() throws Exception {

        Mockito.when(checklistService.deleteChecklist(Mockito.any()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/user/checklist/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

        Mockito.verify(checklistService, Mockito.times(1)).deleteChecklist(1L);
    }
}