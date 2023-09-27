package com.jsantos.bookslist.service.impl;

import com.jsantos.bookslist.dao.BookRepository;
import com.jsantos.bookslist.dao.UserRepository;
import com.jsantos.bookslist.model.Book;
import com.jsantos.bookslist.model.Token;
import com.jsantos.bookslist.model.User;
import com.jsantos.bookslist.model.exception.BookNotFoundException;
import com.jsantos.bookslist.model.exception.UserNotFoundException;
import com.jsantos.bookslist.service.UserService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Slf4j
@Setter
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(long userId) throws UserNotFoundException {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public User save(User user) {
        log.info("Saving user: {}", user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User update(long userId, User user) throws UserNotFoundException {
        if(userRepository.findById(userId).isPresent()) {
            user.setId(userId);
            return userRepository.save(user);
        }
        throw new UserNotFoundException(userId);
    }

    @Override
    public User assignBook(long bookId, long userId) throws BookNotFoundException, UserNotFoundException {
        User user = this.findById(userId);
        Book book = bookRepository.findById(bookId).orElseThrow(() ->new BookNotFoundException(bookId));
        user.getBooks().add(book);
        user.setId(userId);
        return userRepository.save(user);
    }

    @Override
    public void deleteById(long userId) throws UserNotFoundException {
        this.findById(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username: "+ username));
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), authorities);
    }

}
