package com.ey.inspectiontools.service;

import com.ey.inspectiontools.model.User;
import com.ey.inspectiontools.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/** AuthenticationService class to authenticate the user to the system
 *
 * @author Mateus W. Machado
 * @since 09/05/2022
 * @version 1.0.0
 */
@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findByUsername(username);
        if (user.isPresent()){
            return user.get();
        }
        throw new UsernameNotFoundException(username);
    }
}
