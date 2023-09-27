package com.jsantos.bookslist.service;

import com.jsantos.bookslist.model.Book;
import com.jsantos.bookslist.model.Category;
import com.jsantos.bookslist.model.exception.BookNotFoundException;

public interface BookService {
    Iterable<Book> findAll();

    Book findById(long bookId) throws BookNotFoundException;

    Iterable<Book> getAllBooksByAuthor(String author);

    Iterable<Book> getBooksPublishedInCurrentYear();

    Iterable<Book> getAllBooksByPublishedYear(String year);

    Iterable<Book> getBooksByCategory (Category category);

    Book save(Book book);

    Book update(long bookId, Book book) throws BookNotFoundException;

    void deleteById(long bookId) throws BookNotFoundException;

    void delete(Book book);
}
