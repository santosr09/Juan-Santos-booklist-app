package com.jsantos.bookslist.service;

import com.jsantos.bookslist.model.Token;
import com.jsantos.bookslist.model.User;
import com.jsantos.bookslist.model.exception.BookNotFoundException;
import com.jsantos.bookslist.model.exception.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
    Iterable<User> findAll();

    User findById(long userId) throws UserNotFoundException;

    User save(User user);

    User update(long userId, User user) throws UserNotFoundException;

    User assignBook(long bookId, long userId) throws BookNotFoundException, UserNotFoundException;

    void deleteById(long userId) throws UserNotFoundException;

    void delete(User user);

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}
