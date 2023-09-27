package com.jsantos.userservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "users_token")
@Setter
@Getter
public class Token {

    @Id
    @Column(name = "user_id")
    private long id;

    @Column
    private String accessToken;

    @Column
    private String refreshToken;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}
