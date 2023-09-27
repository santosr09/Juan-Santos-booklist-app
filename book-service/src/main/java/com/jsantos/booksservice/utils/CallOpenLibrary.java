package com.jsantos.booksservice.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CallOpenLibrary {

    private final static String BASE_URL = "https://covers.openlibrary.org/b/isbn/";
    private final static String JPG_EXTENSION = ".jpg";
    private final static String MEDIUM_SIZE = "M";

    @Autowired
    public RestTemplate restTemplateCovers;

    public byte[] getImageByIsbnMedium(String isbn) {
        StringBuilder urlStr = new StringBuilder(BASE_URL);
        urlStr.append(isbn).append("-").append(MEDIUM_SIZE).append(JPG_EXTENSION);
        return restTemplateCovers.getForObject(urlStr.toString(), byte[].class);
    }

    public String getUrlforIsbnMedium(String isbn) {
        StringBuilder urlStr = new StringBuilder(BASE_URL);
        return urlStr.append(isbn)
                .append("-")
                .append(MEDIUM_SIZE)
                .append(JPG_EXTENSION)
                .toString();
    }
}