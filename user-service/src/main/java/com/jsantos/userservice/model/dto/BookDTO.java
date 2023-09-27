package com.jsantos.userservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jsantos.userservice.model.Category;
import com.jsantos.userservice.model.User;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

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
