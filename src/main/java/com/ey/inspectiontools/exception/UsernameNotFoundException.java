package com.ey.inspectiontools.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsernameNotFoundException extends Exception {

    public UsernameNotFoundException(String username) {
        super("User not found with username " + username);
    };

}
