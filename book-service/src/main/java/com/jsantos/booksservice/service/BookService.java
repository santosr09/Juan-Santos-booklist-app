package com.jsantos.booksservice.service;

import com.jsantos.booksservice.model.Book;
import com.jsantos.booksservice.model.Category;
import com.jsantos.booksservice.model.dto.BookDTO;
import com.jsantos.booksservice.model.exception.BookNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

public interface BookService {
    Iterable<BookDTO> findAll();

    BookDTO findByBookId(String bookId) throws BookNotFoundException;

    Iterable<BookDTO> findByUser(String userID);

    Iterable<BookDTO> getAllBooksByAuthor(String author);

    Iterable<BookDTO> getBooksPublishedInCurrentYear();

    Iterable<BookDTO> getAllBooksByPublishedYear(String year);

    Iterable<BookDTO> getBooksByCategory (Category category);

    BookDTO save(BookDTO book);

    BookDTO update(String bookId, BookDTO book) throws BookNotFoundException;

    void deleteByBookId(String bookId) throws BookNotFoundException;

    Optional<BookDTO> getOldestBook();

    //byte[] getCover(String bookId) throws BookNotFoundException;
}
