package com.jsantos.userservice.controller;

import com.jsantos.userservice.model.dto.BookDTO;
import com.jsantos.userservice.model.dto.UserDTO;
import com.jsantos.userservice.model.exception.BookNotFoundException;
import com.jsantos.userservice.model.exception.UserNotFoundException;
import com.jsantos.userservice.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Iterable<UserDTO>> getUsers() {
        return status(HttpStatus.OK).body(userService.findAll());
    }

    @GetMapping("/{id}")
    @PostAuthorize("returnObject.body.userId == #id")
    public ResponseEntity<UserDTO> getUser(@PathVariable String id) {
        //SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            return status(HttpStatus.OK).body(userService.findByUserId(id));
        } catch (UserNotFoundException e) {
            return status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}/books")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<BookDTO>> getBooksFromUser(@PathVariable String id, @RequestHeader HttpHeaders headers) {
        return status(HttpStatus.OK).body(userService.getBooksFromUser(id, headers));
    }

    @PostMapping("/{id}/books")
    @PreAuthorize("hasAuthority('WRITER')")
    public ResponseEntity<UserDTO> addBookToUser(@PathVariable String id, @RequestParam(name = "book_id", required = true) String bookId, @RequestHeader HttpHeaders headers) {
        try {
            return status(HttpStatus.OK).body(userService.assignBook(bookId, id, headers));
        } catch (UserNotFoundException | BookNotFoundException e) {
            return status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PostMapping
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO user) {
        log.info("Creating user: " + user);
        try {
            return status(HttpStatus.CREATED).body(userService.save(user));
        } catch (IllegalArgumentException e) {
            return status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('WRITER')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody UserDTO user) {
        try {
            return status(HttpStatus.OK).body(userService.update(id, user));
        } catch (UserNotFoundException e) {
            return status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('WRITER')")
    public ResponseEntity deleteUser(@PathVariable String id) {
        try {
            userService.deleteByUserId(id);
        } catch (UserNotFoundException e) {
            return status(HttpStatus.NOT_FOUND).build();
        }
        return status(HttpStatus.OK).build();
    }

    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

}
