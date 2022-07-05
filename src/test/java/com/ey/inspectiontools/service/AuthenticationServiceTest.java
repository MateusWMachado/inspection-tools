package com.ey.inspectiontools.service;

import com.ey.inspectiontools.dto.UserDTO;
import com.ey.inspectiontools.dto.UserFormDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/** Authentication service test class
 *
 * @author Mateus W. Machado
 * @since 31/05/2022
 * @version 1.0.0
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("Should find user by username, return the User Details and assert all the conditions")
    void loadUserByUsernameTest() {
        UserDTO userFormDTO = UserDTO.builder()
                .created_at(LocalDate.now())
                .password("MockAccount")
                .role("USER")
                .username("MockAccount")
                .build();

        userService.createAccount(userFormDTO, UriComponentsBuilder.newInstance());

        UserDetails foundedUserDetails = authenticationService.loadUserByUsername(userFormDTO.getUsername());

        assertNotNull(foundedUserDetails);
        assertEquals(userFormDTO.getUsername(), foundedUserDetails.getUsername());
        assertEquals(userFormDTO.getPassword(), foundedUserDetails.getPassword());
    }

    @Test
    @DisplayName("Should throw an UsernameNotFoundException if can not find the user by username")
    void shouldThrowNewExceptionIfCanNotFindUserByUsername() {
        assertThrows(UsernameNotFoundException.class, () -> authenticationService.loadUserByUsername("UsernameNotExistent"));
    }
}