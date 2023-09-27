package com.jsantos.userservice.service;

import com.jsantos.userservice.model.User;
import com.jsantos.userservice.model.dto.UserDTO;
import com.jsantos.userservice.model.exception.BookNotFoundException;
import com.jsantos.userservice.model.exception.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
    Iterable<UserDTO> findAll();

    UserDTO findByUserId(String userId) throws UserNotFoundException;

    UserDTO save(UserDTO user);

    UserDTO update(String userId, UserDTO user) throws UserNotFoundException;

    UserDTO assignBook(String bookId, String userId, HttpHeaders headers) throws BookNotFoundException, UserNotFoundException;

    void deleteByUserId(String userId) throws UserNotFoundException;

    void delete(UserDTO user);

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}
