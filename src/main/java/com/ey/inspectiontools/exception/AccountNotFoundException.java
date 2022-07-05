package com.ey.inspectiontools.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends Exception{

    public AccountNotFoundException(Long id) {
        super("Account not found with id: " + id);
    }
}
