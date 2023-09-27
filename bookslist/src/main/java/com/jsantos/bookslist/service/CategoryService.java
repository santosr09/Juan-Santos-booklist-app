package com.jsantos.bookslist.service;

import com.jsantos.bookslist.model.Category;
import com.jsantos.bookslist.model.exception.BookNotFoundException;
import com.jsantos.bookslist.model.exception.CategoryNotFoundException;

import java.util.List;

public interface CategoryService {
    Iterable<Category> findAll();

    Category findById(long categoryId) throws CategoryNotFoundException;

    List<String> getBooksTitlesByCategory(String categoryName);

    Category save(Category category);

    Category update(long categoryId, Category category) throws CategoryNotFoundException;

    Category assignToBook(long bookId, long categoryId) throws CategoryNotFoundException, BookNotFoundException;

    void deleteById(long categoryId) throws CategoryNotFoundException;

    void delete(Category category);
}
