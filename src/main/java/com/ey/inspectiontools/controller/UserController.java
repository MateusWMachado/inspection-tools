package com.ey.inspectiontools.controller;

import com.ey.inspectiontools.dto.UserDTO;
import com.ey.inspectiontools.dto.UserFormDTO;
import com.ey.inspectiontools.exception.AccountNotFoundException;
import com.ey.inspectiontools.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

/** Class that represents a controller to handle information from Users
 *
 * @author Mateus W. Machado
 * @since 24/05/2022
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private UserService service;
    @GetMapping
    @RequestMapping("/admin/account")
    public Page<UserDTO> findAll(Pageable pageable) {
        return service.findAll(pageable);
    }
    @GetMapping
    @RequestMapping("/admin/account/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) throws AccountNotFoundException {
        return service.findAccountById(id);
    }
    @PostMapping
    @RequestMapping("/rh/create-account")
    public ResponseEntity<String> createAccountData(@RequestBody @Valid UserDTO user, UriComponentsBuilder uriBuilder) {
        return service.createAccount(user, uriBuilder);
    }
    @DeleteMapping("/admin/account/{id}")
    public ResponseEntity<String> deleteAccountData(@PathVariable Long id) throws AccountNotFoundException {
        return service.deleteAccount(id);
    }
    @PutMapping("/admin/account/{id}")
    public ResponseEntity<UserDTO> updateAccountData(@PathVariable Long id, @RequestBody UserDTO user) throws AccountNotFoundException{
        return service.updateAccount(id, user);
    }
}

