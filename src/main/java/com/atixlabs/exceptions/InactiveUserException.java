package com.atixlabs.exceptions;

import com.atixlabs.model.configuration.User;
import lombok.Getter;

@Getter
public class InactiveUserException extends Exception {

    private User user;

    public InactiveUserException(User user) {
        this.user = user;
    }
}
