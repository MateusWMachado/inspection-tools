package com.ey.inspectiontools.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ChecklistNotFoundException extends Exception{

    public ChecklistNotFoundException(Long id) {
        super("Checklist not found with id: " + id );
    }

}
