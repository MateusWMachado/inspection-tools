package com.ey.inspectiontools.service;

import com.ey.inspectiontools.dto.UserDTO;
import com.ey.inspectiontools.dto.UserFormDTO;
import com.ey.inspectiontools.exception.AccountNotFoundException;
import com.ey.inspectiontools.model.User;
import com.ey.inspectiontools.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

/** UserService contract implementation class
 *
 * @author Mateus W. Machado
 * @since 24/05/2022
 * @version 1.0.0
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private UserRepository repository;
    private ModelMapper mapper;

    /** Method to find all users accounts
     *
     * @author Mateus W. Machado
     * @param pageable default pagination of the Pageable class
     * @return a list of UserDTO with pagination
     */
    @Override
    public Page<UserDTO> findAll(Pageable pageable) {
        Page<User> all = repository.findAll(pageable);
        log.debug("UserServiceImpl.findAll - end - founded accounts: [{}]", all);

        return new PageImpl<>(all.stream()
                .map(account -> mapper.map(account, UserDTO.class))
                .collect(Collectors.toList()));
    }

    /** Method to find an account by ID
     *
     * @author Mateus W. Machado
     * @param id the ID from User
     * @return if the method finds a user it must return a HttpStatus - OK and the UserDTO, if the method does not found it must return a AccountNotFoundException
     */
    @Override
    public ResponseEntity<UserDTO> findAccountById (Long id) throws AccountNotFoundException {
        User user = verifyIfAccountExists(id);
        log.debug("UserServiceImpl.findAccountById - end - founded user: [{}]", user);
        return ResponseEntity.ok(mapper.map(user, UserDTO.class));
    }

    /** Method to delete an account by ID
     *
     * @author Mateus W. Machado
     * @param id the ID from User
     * @return if the method finds a user it must delete the account and return a HttpStatus - OK, if the method does not found it must return a AccountNotFoundException
     */
    @Override
    public ResponseEntity<String> deleteAccount(Long id) throws AccountNotFoundException {
        User user = verifyIfAccountExists(id);
        log.debug("UserServiceImpl.deleteAccount - end - founded user: [{}]", user);
        repository.delete(user);
        return ResponseEntity.ok().build();
    }

    /** Method to update an account by ID
     *
     * @author Mateus W. Machado
     * @param id the ID from User
     * @param user updated UserDTO form
     * @return if the method finds a user it must update the account, return a HttpStatus - OK and the updated UserDTO, if the method does not found it must return a AccountNotFoundException
     */
    @Override
    public ResponseEntity<UserDTO> updateAccount(Long id, UserDTO user) throws AccountNotFoundException {
        User foundedUser = verifyIfAccountExists(id);
        return repository.findById(foundedUser.getId()).map(account -> {
            account.setUsername(user.getUsername());
            account.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            account.setUpdated_at(LocalDate.now());
            User savedAccount = repository.save(account);
            log.debug("UserServiceImpl.updateAccount - end - saved account: [{}]", savedAccount);
            return ResponseEntity.ok(mapper.map(savedAccount, UserDTO.class));
        }).orElse(ResponseEntity.notFound().build());
    }

    /** Method to create an account
     *
     * @author Mateus W. Machado
     * @param user the user form
     * @param uriBuilder used for constructing URI
     * @return the method must encrypt the user password, create the account, return a HttpStatus - CREATED and a string with a message
     */
    @Override
    public ResponseEntity<String> createAccount(UserDTO user, UriComponentsBuilder uriBuilder) {
        user.setCreated_at(LocalDate.now());
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));


        User savedUser = repository.save(mapper.map(user, User.class));




        URI uri = uriBuilder.path("/api/v1/rh/create-account/{id}").buildAndExpand(savedUser.getId()).toUri();

        log.debug("UserServiceImpl.createAccount - end - saved user: [{}]", savedUser);

        return ResponseEntity.created(uri).body("Created account with username: " + savedUser.getUsername());
    }

    /** Method to verify if the account exists
     *
     * @author Mateus W. Machado
     * @param id the ID from User
     * @return if the method finds a user it must return the account, if the method does not found it must return a AccountNotFoundException
     */
    private User verifyIfAccountExists(Long id) throws AccountNotFoundException {
        return repository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
    }


}
