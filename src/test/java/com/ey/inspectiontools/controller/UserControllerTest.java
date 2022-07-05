package com.ey.inspectiontools.controller;

import com.ey.inspectiontools.dto.UserDTO;
import com.ey.inspectiontools.dto.UserFormDTO;
import com.ey.inspectiontools.exception.AccountNotFoundException;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URI;
import java.net.URLEncoder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** User Controller test class
 *
 * @author Mateus W. Machado
 * @since 31/05/2022
 * @version 1.0.0
 */
@WebMvcTest(UserController.class)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ChecklistService checklistService;

    @MockBean
    private ImageController imageController;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    @WithMockUser(username = "mockUser", password = "mockPassword", authorities = "USER")
    @DisplayName("Should throw an exception if user do not have authorities")
    void shouldThrowExceptionIfUserDoNotHaveAuthorities() throws Exception {
        Mockito.when(userService.findAccountById(1L)).thenReturn(new ResponseEntity<>(HttpStatus.FORBIDDEN));

        mockMvc.perform(get("/api/v1/admin/account/{id}", "1"))
                .andExpect(status().isForbidden());

        Mockito.verify(userService, Mockito.times(0)).findAccountById(1L);

    }
    @Test
    @WithMockUser(username = "user2", password = "user2", authorities = "ADMIN")
    @DisplayName("Should execute a GET request on endpoint '/api/v1/admin/account/{id}' with user authorities - ADMIN and call userService method 'findAccountById(ID)'")
    void findAccountByIdTest() throws Exception {

        UserDTO userDTO = UserDTO.builder()
                .role("ADMIN")
                .username("UnitTest")
                .build();

        Mockito.when(userService.findAccountById(1L)).thenReturn(ResponseEntity.ok(userDTO));

        mockMvc.perform(get("/api/v1/admin/account/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(("username")).value(userDTO.getUsername()))
                .andExpect(jsonPath(("role")).value(userDTO.getRole()));

        Mockito.verify(userService, Mockito.times(1)).findAccountById(1L);

    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = "ADMIN")
    @DisplayName("Should throw an AccountNotFoundException if can not find the user by Id")
    void shouldThrowAccountNotFoundExceptionWhenUserNotFind() throws Exception {
        Mockito.when(userService.findAccountById(Mockito.anyLong())).thenThrow(AccountNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/admin/account/{id}", "100")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        Mockito.verify(userService, Mockito.times(0)).findAccountById(1L);
    }

    @Test
    @WithMockUser(username = "user2", password = "user2", authorities = "ADMIN")
    @DisplayName("Should execute a GET request on endpoint '/api/v1/admin/account' with user authorities - ADMIN and call userService method 'findAll(PageRequest.class)'")
    void findAllAccountsTest() throws Exception {

        mockMvc.perform(get("/api/v1/admin/account")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).findAll(Mockito.any(PageRequest.class));

    }

    @Test
    @WithMockUser(username = "user2", password = "user2", authorities = "RH")
    @DisplayName("Should execute a POST request on endpoint '/api/v1/rh/create-account' with user authorities - RH and call userService method 'createAccount()'")
    void createAccountTest() throws Exception {

        UserFormDTO userFormDTO = UserFormDTO.builder()
                .password("321321")
                .role("ADMIN")
                .username("UnitTest")
                .build();

        Mockito.when(userService.createAccount(Mockito.any(), Mockito.any()))
                        .thenReturn(ResponseEntity
                                .created(URI.create(URLEncoder.encode("/api/v1/rh/create-account/{username}")))
                                .body("Created account with username: " + userFormDTO.getUsername()));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userFormDTO);

        RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/rh/create-account")
                .accept(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().string("Created account with username: " + userFormDTO.getUsername()));

    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = "ADMIN")
    @DisplayName("Should execute a DELETE request on endpoint '/api/v1/admin/account/{id}' with user authorities - ADMIN and call userService method 'deleteAccount(ID)'")
    void deleteAccountTest() throws Exception {

        Mockito.when(userService.deleteAccount(Mockito.any()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/admin/account/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).deleteAccount(1L);
    }

    @Test
    @WithMockUser(username = "test", password = "test", authorities = "ADMIN")
    @DisplayName("Should execute a PUT request on endpoint '/api/v1/admin/account/{id}' with user authorities - ADMIN and call userService method 'updateAccount(ID, UserFormDTO.class)'")
    void updateAccountTest() throws Exception {

        UserDTO userDTO = UserDTO.builder()
                .role("ADMIN")
                .username("UnitTest")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/admin/account/{id}", "1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).updateAccount(1L, userDTO);
    }
}