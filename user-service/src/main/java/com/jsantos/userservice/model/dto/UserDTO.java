package com.jsantos.userservice.model.dto;

import com.jsantos.userservice.model.Book;
import com.jsantos.userservice.model.Rol;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String userId;
    //@NotNull
    //@Size(min=2, message = "{validation.firstname.size}")
    private String firstName;
    //@NotNull
    //@Size(min=2, message = "{validation.firstname.size}")
    private String lastName;
    private String country;
    private String registrationDate;
    private String username;
    private String password;
    private Set<BookDTO> books;
    private Set<RolDTO> roles;

}
