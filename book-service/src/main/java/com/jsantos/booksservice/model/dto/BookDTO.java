package com.jsantos.booksservice.model.dto;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    private String bookId;
    private String name;
    private String description;
    private String publisher;
    private String author;
    private String isbn;
    private LocalDate publishedDate;
    private String coverUrl;
}
