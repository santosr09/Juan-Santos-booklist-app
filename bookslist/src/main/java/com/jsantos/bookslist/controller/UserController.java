package com.jsantos.bookslist.controller;

import com.jsantos.bookslist.model.Book;
import com.jsantos.bookslist.model.User;
import com.jsantos.bookslist.model.exception.BookNotFoundException;
import com.jsantos.bookslist.model.exception.UserNotFoundException;
import com.jsantos.bookslist.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public ResponseEntity<Iterable<User>> getUsers() {
        return status(HttpStatus.OK).body(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        try {
            return status(HttpStatus.OK).body(userService.findById(id));
        } catch (UserNotFoundException e) {
            return status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<Set<Book>> getBooksFromUser(@PathVariable long id) {
        try {
            User user = userService.findById(id);
            return status(HttpStatus.OK).body(user.getBooks());
        } catch (UserNotFoundException e) {
            return status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{id}/books")
    public ResponseEntity<User> addBookToUser(@PathVariable long id, @RequestParam(name = "book_id", required = true) long bookId) {
        try {
            return status(HttpStatus.OK).body(userService.assignBook(bookId, id));
        } catch (UserNotFoundException | BookNotFoundException e) {
            return status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            return status(HttpStatus.CREATED).body(userService.save(user));
        } catch (IllegalArgumentException e) {
            return status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody User user) {
        try {
            return status(HttpStatus.OK).body(userService.update(id, user));
        } catch (UserNotFoundException e) {
            return status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable long id) {
        try {
            userService.deleteById(id);
        } catch (UserNotFoundException e) {
            return status(HttpStatus.NOT_FOUND).build();
        }
        return status(HttpStatus.OK).build();
    }

    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

}
