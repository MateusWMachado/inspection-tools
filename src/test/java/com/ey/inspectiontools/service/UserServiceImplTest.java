package com.ey.inspectiontools.service;

import com.ey.inspectiontools.dto.UserDTO;
import com.ey.inspectiontools.dto.UserFormDTO;
import com.ey.inspectiontools.exception.AccountNotFoundException;
import com.ey.inspectiontools.model.User;
import com.ey.inspectiontools.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/** User service test class
 *
 * @author Mateus W. Machado
 * @since 31/05/2022
 * @version 1.0.0
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceImplTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository repository;
    @Autowired
    private ModelMapper mapper;

    private User userBody;
    @BeforeEach
    void createAccountBeforeEachTestAndFindTheAccountByUsernameSetup() {
        UserDTO userFormDTO = UserDTO.builder()
                .created_at(LocalDate.now())
                .password("MockPassword")
                .role("USER")
                .username("MockUsername")
                .build();

        userService.createAccount(userFormDTO, UriComponentsBuilder.newInstance());

        this.userBody = repository.findByUsername(userFormDTO.getUsername()).orElse(null);
    }
    @Test
    @DisplayName("Should find all accounts, return status code - OK and assert all the conditions")
    void findAllAccountsTest() {
        Page<UserDTO> allAccounts = userService.findAll(Pageable.unpaged());

        assertNotNull(allAccounts);
        assertTrue(allAccounts.getTotalElements() > 0);

    }

    @Test
    @DisplayName("Should find an account by ID, return status code - OK and assert all the conditions")
    void findAccountByIDTest() throws AccountNotFoundException {
        ResponseEntity<UserDTO> accountById = userService.findAccountById(this.userBody.getId());

        assertNotNull(accountById);
        assertEquals(HttpStatus.OK, accountById.getStatusCode());
        assertEquals(this.userBody.getUsername(), Objects.requireNonNull(accountById.getBody()).getUsername());
        assertEquals(this.userBody.getPassword(), Objects.requireNonNull(accountById.getBody()).getPassword());
        assertEquals(this.userBody.getRole(), Objects.requireNonNull(accountById.getBody()).getRole());
    }

    @Test
    @DisplayName("Should throw an AccountNotFoundException if can not find the account by ID")
    void shouldThrowAnExceptionIfCanNotFindTheAccountByID() {
        assertThrows(AccountNotFoundException.class, () -> userService.findAccountById(0L));
    }

    @Test
    @DisplayName("Should delete the account with the ID, return status code - OK and assert the condition")
    void deleteAccountTest() throws AccountNotFoundException {
        ResponseEntity<String> account = userService.deleteAccount(this.userBody.getId());

        assertEquals(HttpStatus.OK, account.getStatusCode());
    }

    @Test
    @DisplayName("Should throw an AccountNotFoundException if can not delete the account")
    void shouldThrowAnExceptionIfCanNotDeleteTheAccountByID() {
        assertThrows(AccountNotFoundException.class, () -> userService.deleteAccount(0L));
    }

    @Test
    @DisplayName("Should update the account, return status code - OK and assert all the conditions")
    void updateAccountTest() throws AccountNotFoundException {

        UserDTO userToUpdate = UserDTO.builder()
                .updated_at(LocalDate.now())
                .password("updateAccount")
                .username("updateAccount")
                .role("USER")
                .build();

        ResponseEntity<UserDTO> updateAccountTest = userService.updateAccount(this.userBody.getId(), userToUpdate);

        assertNotNull(updateAccountTest);
        assertEquals(HttpStatus.OK, updateAccountTest.getStatusCode());
        assertEquals(userToUpdate.getUsername(), Objects.requireNonNull(updateAccountTest.getBody()).getUsername());
        assertEquals(userToUpdate.getRole(), Objects.requireNonNull(updateAccountTest.getBody()).getRole());

    }

    @Test
    @DisplayName("Should create the account, return status code - CREATED and assert all the conditions")
    void createAccountTest() {
        UserDTO userFormTest = UserDTO.builder()
                            .created_at(LocalDate.now())
                            .password("321321")
                            .role("ADMIN")
                            .username("UnitTest")
                            .build();

        ResponseEntity<String> createAccount = userService.createAccount(userFormTest, UriComponentsBuilder.newInstance());

        assertNotNull(createAccount);
        assertEquals(HttpStatus.CREATED, createAccount.getStatusCode());
        assertEquals("Created account with username: " + userFormTest.getUsername(), createAccount.getBody());
    }
}