package com.jsantos.userservice.model.exception;

import com.jsantos.userservice.utils.Messages;
import org.springframework.stereotype.Component;

@Component
public class UserNotFoundException extends RuntimeException{

    private final static String message = Messages.getMessagesForKey("exception.usernotfound");

    public UserNotFoundException() {
        super(message);
    }

    public UserNotFoundException(String userId) {
        super(message.concat("| User ID: " + userId));
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
