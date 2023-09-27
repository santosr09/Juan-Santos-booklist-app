package com.jsantos.booksservice.model.exception;


import com.jsantos.booksservice.utils.Messages;
import org.springframework.stereotype.Component;

@Component
public class UserNotFoundException extends RuntimeException{

    private final static String message = Messages.getMessagesForKey("exception.usernotfound");

    public UserNotFoundException() {
        super(message);
    }

    public UserNotFoundException(long userId) {
        super(message.concat("| User ID: " + userId));
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
