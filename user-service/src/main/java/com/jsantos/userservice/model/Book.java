package com.jsantos.userservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "books")
@Getter
@Setter
@ToString
public class Book {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "author")
    private String author;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "published_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate publishedDate;

    @JsonIgnore
    @ManyToMany(mappedBy = "books")
    private Set<Category> categories;

/*    @JsonIgnore
    @ManyToMany(mappedBy = "books", fetch = FetchType.LAZY)
    private Set<User> users;*/

    @Column(name = "cover_url")
    private String coverUrl;

}
