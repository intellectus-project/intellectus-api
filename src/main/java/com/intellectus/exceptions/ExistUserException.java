package com.intellectus.exceptions;

import com.intellectus.model.configuration.User;
import lombok.Getter;

@Getter
public class ExistUserException extends Exception{

    private User user;

    public ExistUserException(User user) {
        this.user = user;
    }
}
