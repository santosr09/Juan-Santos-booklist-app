package com.jsantos.userservice.model.exception;

import com.jsantos.userservice.utils.Messages;
import org.springframework.stereotype.Component;

@Component
public class CategoryNotFoundException extends Exception{

    private final static String message = Messages.getMessagesForKey("exception.categorynotfound");

    public CategoryNotFoundException() {
        super(message);
    }

    public CategoryNotFoundException(long categoryId) {
        super(message.concat("| Category Id: " + categoryId));
    }

    public CategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
