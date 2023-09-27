package com.jsantos.booksservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@ToString
public class Rol {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "name")
    private String name;

    @JsonIgnore //To avoid stackoverflow, Investigate more about this
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}
