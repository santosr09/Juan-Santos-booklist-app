package com.jsantos.booksservice.service;

import com.jsantos.booksservice.model.Category;
import com.jsantos.booksservice.model.exception.BookNotFoundException;
import com.jsantos.booksservice.model.exception.CategoryNotFoundException;

import java.util.List;

public interface CategoryService {
    Iterable<Category> findAll();

    Category findById(long categoryId) throws CategoryNotFoundException;

    List<String> getBooksTitlesByCategory(String categoryName);

    Category save(Category category);

    Category update(long categoryId, Category category) throws CategoryNotFoundException;

    Category assignToBook(long categoryId, String bookId) throws CategoryNotFoundException, BookNotFoundException;

    void deleteById(long categoryId) throws CategoryNotFoundException;

    void delete(Category category);
}
