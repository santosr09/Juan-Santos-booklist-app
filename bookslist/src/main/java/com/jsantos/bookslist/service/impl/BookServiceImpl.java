package com.jsantos.bookslist.service.impl;

import com.jsantos.bookslist.model.Category;
import com.jsantos.bookslist.service.BookService;
import com.jsantos.bookslist.utils.Messages;
import com.jsantos.bookslist.dao.BookRepository;
import com.jsantos.bookslist.model.Book;
import com.jsantos.bookslist.model.exception.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository repository;

    @Override
    public Iterable<Book> findAll() {
        return repository.findAll();
    }

    @Override
    public Book findById(long bookId) throws BookNotFoundException {
        return repository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
    }

    @Override
    public Iterable<Book> getAllBooksByAuthor(String author) {
        Stream<Book> books = StreamSupport.stream(repository.findAll().spliterator(), false);
        return books.filter(b -> author.equals(b.getAuthor()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Book> getBooksPublishedInCurrentYear() {
        LocalDate currentDate = LocalDate.now();
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(book -> book.getPublishedDate().getYear() == currentDate.getYear())
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Book> getAllBooksByPublishedYear(String year) {
        LocalDate currentDate = LocalDate.now();
        if(Integer.parseInt(year) > currentDate.getYear())
            throw new RuntimeException(Messages.getMessagesForKey("error.notavalidyear"));
        return repository.findByPublishedYear(Integer.parseInt(year));
    }

    @Override
    public Iterable<Book> getBooksByCategory(Category category) {
        Stream<Book> books = StreamSupport.stream(repository.findAll().spliterator(), false);
        return books.filter(b -> b.getCategories().contains(category))
                .collect(Collectors.toList());
    }

    public Optional<Book> getOldestBook() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .min(Comparator.comparing(Book::getPublishedDate));
    }

    @Override
    public Book save(Book book) {
        return repository.save(book);
    }

    @Override
    public Book update(long bookId, Book book) throws BookNotFoundException {
        if (repository.findById(bookId).isPresent()){
            book.setId(bookId);
            return repository.save(book);
        }
        throw new BookNotFoundException(bookId);
    }

    @Override
    public void deleteById(long bookId) throws BookNotFoundException {
        this.findById(bookId);
        repository.deleteById(bookId);
    }

    @Override
    public void delete(Book book) {
        repository.delete(book);
    }

    public void setRepository(BookRepository repository) {
        this.repository = repository;
    }

}