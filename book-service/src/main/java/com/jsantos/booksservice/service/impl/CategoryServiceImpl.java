package com.jsantos.booksservice.service.impl;

import com.jsantos.booksservice.dao.BookRepository;
import com.jsantos.booksservice.dao.CategoryRepository;
import com.jsantos.booksservice.model.Book;
import com.jsantos.booksservice.model.Category;
import com.jsantos.booksservice.model.exception.BookNotFoundException;
import com.jsantos.booksservice.model.exception.CategoryNotFoundException;
import com.jsantos.booksservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    BookRepository bookRepository;

    @Override
    public Iterable<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(long categoryId) throws CategoryNotFoundException {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    @Override
    public List<String> getBooksTitlesByCategory(String categoryName) {
        Category category = categoryRepository.findByName(categoryName);
        return category.getBooks().stream()
                .map(Book::getName)
                .collect(Collectors.toList());
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category update(long categoryId, Category category) throws CategoryNotFoundException {
        if(categoryRepository.findById(categoryId).isPresent()) {
            category.setId(categoryId);
            return categoryRepository.save(category);
        }
        throw new CategoryNotFoundException(categoryId);
    }

    @Override
    public Category assignToBook(long categoryId, String bookId) throws CategoryNotFoundException, BookNotFoundException {
        Category category = this.findById(categoryId);
        Book book = bookRepository.findByBookId(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
        category.getBooks().add(book);
        category.setId(categoryId);
        return categoryRepository.save(category);
    }

    @Override
    public void deleteById(long categoryId) throws CategoryNotFoundException {
        this.findById(categoryId);
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public void delete(Category category) {
        categoryRepository.delete(category);
    }

    public void setRepository(CategoryRepository repository) {
        this.categoryRepository = repository;
    }

    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

}
