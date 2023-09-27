package com.jsantos.bookslist.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsantos.bookslist.model.Category;
import com.jsantos.bookslist.model.exception.BookNotFoundException;
import com.jsantos.bookslist.model.exception.CategoryNotFoundException;
import com.jsantos.bookslist.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.StreamSupport;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryServiceImpl service;

    @GetMapping
    public ResponseEntity<Iterable<Category>> getCategories() {
        Iterable<Category> categories = service.findAll();
        if (StreamSupport.stream(categories.spliterator(), false).count() == 0)
            return status(HttpStatus.NO_CONTENT).body(categories);
        return status(HttpStatus.OK).body(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable long id) {
        try {
            return status(HttpStatus.OK).body(service.findById(id));
        } catch (CategoryNotFoundException e) {
            return status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        try {
            return status(HttpStatus.CREATED).body(service.save(category));
        } catch (IllegalArgumentException e) {
            return status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable long id, @RequestBody Category category) {
        try {
            return status(HttpStatus.OK).body(service.update(id, category));
        } catch (CategoryNotFoundException e) {
            return status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{categoryId}")
    public ResponseEntity<Category> assignCategoryToBook(@PathVariable long categoryId, @RequestBody String bookId) {
        try {
            ObjectMapper om = new ObjectMapper();
            JsonNode node = om.readTree(bookId);
            node.toString();
            node.get("bookId");
            return status(HttpStatus.OK).body(service.assignToBook(categoryId, node.get("book_id").asLong()));
        } catch (BookNotFoundException | CategoryNotFoundException | JsonProcessingException e) {
            return status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCategory(@PathVariable long id) {
        try {
            service.deleteById(id);
            return status(HttpStatus.OK).build();
        } catch (CategoryNotFoundException e) {
            return status(HttpStatus.NOT_FOUND).build();
        }
    }

}
