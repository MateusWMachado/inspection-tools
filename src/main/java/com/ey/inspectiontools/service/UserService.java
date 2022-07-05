package com.ey.inspectiontools.service;

import com.ey.inspectiontools.dto.UserDTO;
import com.ey.inspectiontools.dto.UserFormDTO;
import com.ey.inspectiontools.exception.AccountNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

/** Interface representing a User service operations contract
 *
 * @author Mateus W. Machado
 * @since 24/05/2022
 * @version 1.0.0
 */
public interface UserService {

    Page<UserDTO> findAll(Pageable pageable);
    ResponseEntity<UserDTO> findAccountById (Long id) throws AccountNotFoundException;
    ResponseEntity<String> deleteAccount(Long id) throws AccountNotFoundException;
    ResponseEntity<UserDTO> updateAccount(Long id, UserDTO user) throws AccountNotFoundException;
    ResponseEntity<String> createAccount(UserDTO user, UriComponentsBuilder uriBuilder);

}
