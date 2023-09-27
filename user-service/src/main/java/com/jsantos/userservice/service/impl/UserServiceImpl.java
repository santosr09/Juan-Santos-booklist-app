package com.jsantos.userservice.service.impl;

import com.jsantos.userservice.dao.UserRepository;
import com.jsantos.userservice.dao.feign.BooksServiceClient;
import com.jsantos.userservice.model.User;
import com.jsantos.userservice.model.dto.BookDTO;
import com.jsantos.userservice.model.dto.UserDTO;
import com.jsantos.userservice.model.exception.BookNotFoundException;
import com.jsantos.userservice.model.exception.UserNotFoundException;
import com.jsantos.userservice.model.mappers.UserMapper;
import com.jsantos.userservice.service.UserService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
@Setter
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BooksServiceClient booksServiceClient;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Iterable<UserDTO> findAll() {
        return UserMapper.INSTANCE.listOfEntitiesToListOfDtos(userRepository.findAll());
    }

    @Override
        public UserDTO findByUserId(String userId) throws UserNotFoundException {
        UserDTO dto = UserMapper.INSTANCE.entityToDto(
                userRepository.findByUserId(userId).orElseThrow(() -> new UserNotFoundException(userId))
        );
        return dto;
    }

    @Override
    public UserDTO save(UserDTO user) {
        log.info("Saving user: {}", user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserId(UUID.randomUUID().toString());
        User entity = UserMapper.INSTANCE.dtoToEntity(user);
        userRepository.save(entity);
        return user;
    }

    @Override
    public UserDTO update(String userId, UserDTO user) throws UserNotFoundException {
        if(userRepository.findByUserId(userId).isPresent()) {
            User entity = userRepository.findByUserId(userId).get();
            //userRepository.save(UserMapper.INSTANCE.dtoToEntity(user));
            User updatedUser = userRepository.save(UserMapper.INSTANCE.updateUserFromDto(user, entity));


            return UserMapper.INSTANCE.entityToDto(updatedUser);
        }
        throw new UserNotFoundException(userId);
    }

    @Override
    public UserDTO assignBook(String bookId, String userId, HttpHeaders headers) throws BookNotFoundException, UserNotFoundException {
        User entity = userRepository.findByUserId(userId).get();
        UserDTO dto = this.findByUserId(userId);
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("bookID", bookId);

        try {
            ResponseEntity<BookDTO> response = booksServiceClient.getBook(bookId);
            dto.getBooks().add(response.getBody());
            entity = UserMapper.INSTANCE.updateUserFromDto(dto, entity);
            entity.getId();
            userRepository.save(entity);

        } catch ( Exception ex) {
            log.debug("Exception in assignBook(): " + ex);
            ex.printStackTrace();
        }


        return UserMapper.INSTANCE.entityToDto(entity);
    }

    @Transactional
    @Override
    public void deleteByUserId(String userId) throws UserNotFoundException {
        this.findByUserId(userId);
        userRepository.deleteByUserId(userId);
    }

    @Override
    public void delete(UserDTO user) {
        userRepository.delete(UserMapper.INSTANCE.dtoToEntity(user));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username: "+ username));
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), authorities);
    }

    public List<BookDTO> getBooksFromUser(String userID, HttpHeaders headers) {
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("User_ID", userID);

        log.info("Before Calling book service");
        ResponseEntity<Iterable<BookDTO>> response = (ResponseEntity<Iterable<BookDTO>>) booksServiceClient.getBooks(uriParams, headers);
        log.info("AFTER Calling book service");
        List<BookDTO> books = (List<BookDTO>) response.getBody();
        return books;

    }

}
