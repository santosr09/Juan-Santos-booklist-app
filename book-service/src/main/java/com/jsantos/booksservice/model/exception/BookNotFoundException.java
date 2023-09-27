package com.jsantos.booksservice.model.exception;

import com.jsantos.booksservice.utils.Messages;
import org.springframework.stereotype.Component;

@Component
public class BookNotFoundException extends Exception {

    private final static String message = Messages.getMessagesForKey("exception.booknotfound");

    public BookNotFoundException() {
        super(message);
    }

    public BookNotFoundException(String bookId) {
        super(message.concat("| Book ID: " + bookId));
    }

    public BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
