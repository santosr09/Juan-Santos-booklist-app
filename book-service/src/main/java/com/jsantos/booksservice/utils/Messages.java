package com.jsantos.booksservice.utils;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
public class Messages {

    public static String getMessagesForKey(String messageKey) {
        return ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale()).getString(messageKey);
    }

}
