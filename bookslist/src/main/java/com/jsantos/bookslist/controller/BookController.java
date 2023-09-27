package com.jsantos.bookslist.controller;

import com.jsantos.bookslist.model.Book;
import com.jsantos.bookslist.model.exception.BookNotFoundException;
import com.jsantos.bookslist.service.CategoryService;
import com.jsantos.bookslist.service.impl.BookServiceImpl;
import com.jsantos.bookslist.utils.BookFilters;
import com.jsantos.bookslist.utils.CallOpenLibrary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.jsantos.bookslist.utils.BookFilters.AUTHOR;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookServiceImpl bookServiceImpl;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    CallOpenLibrary callOpenLibrary;

    @GetMapping
    public ResponseEntity<? extends Object> getBooks(@RequestParam Map<String, String> queryParams) {
        if(!queryParams.isEmpty()){
            return getBooksFiltered(queryParams);
        }

        //Without filters
        Iterable<Book> books = bookServiceImpl.findAll();
        if(StreamSupport.stream(books.spliterator(), false).findAny().isEmpty()){
            return status(HttpStatus.NO_CONTENT).build();
        }
        return status(HttpStatus.OK).body(books);
    }

    private ResponseEntity<? extends Object> getBooksFiltered(Map<String, String> params) {

        Optional<String> filter = params.keySet().stream().findFirst();
        if (filter.isPresent()) {
            switch (BookFilters.valueOf(filter.get().toUpperCase())){
                case AUTHOR:
                    return getBooksByAuthor(params.get(AUTHOR.getFilterName()));
                case CATEGORY:
                    return getBooksByCategory(params.get(BookFilters.CATEGORY.getFilterName()));
                case CURRENT_YEAR:
                    return getBooksPublishedInCurrentYear();
                case OLDEST_BOOK:
                    return getOldestBook();
                default:
                    return status(HttpStatus.NO_CONTENT).build();
            }
        }
        return status(HttpStatus.NO_CONTENT).build();
    }

    private ResponseEntity<Iterable<Book>> getBooksPublishedInCurrentYear() {
        Iterable<Book> books = bookServiceImpl.getBooksPublishedInCurrentYear();
        if(StreamSupport.stream(books.spliterator(), false).findAny().isEmpty()){
            return status(HttpStatus.NO_CONTENT).build();
        }
        return status(HttpStatus.OK).body(books);
    }

    private ResponseEntity<Iterable<Book>> getBooksByAuthor(String author) {
        Iterable<Book> books = bookServiceImpl.getAllBooksByAuthor(author);
        if(StreamSupport.stream(books.spliterator(), false).findAny().isEmpty()){
            return status(HttpStatus.NO_CONTENT).build();
        }
        return status(HttpStatus.OK).body(books);
    }

    private ResponseEntity<Iterable<String>> getBooksByCategory(String categoryName) {
        List<String> booksTitles = categoryService.getBooksTitlesByCategory(categoryName);
        if(!booksTitles.isEmpty()) {
            return status(HttpStatus.OK).body(booksTitles);
        }
        return status(HttpStatus.NO_CONTENT).build();
    }

    private ResponseEntity<Book> getOldestBook() {
        Optional<Book> book = bookServiceImpl.getOldestBook();
        if(book.isPresent())
            return status(HttpStatus.OK).body(book.get());
        return status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{bookId}/covers")
    public ResponseEntity<byte[]> getCover(@PathVariable long bookId) {
        HttpHeaders headers = new HttpHeaders();
        Book book;
        try {
            book = bookServiceImpl.findById(bookId);
        } catch (BookNotFoundException e) {
            return status(HttpStatus.NO_CONTENT).build();
        }
        headers.setContentType(MediaType.IMAGE_JPEG);
        byte[] in = callOpenLibrary.getImageByIsbnMedium(book.getIsbn());

        if( in == null || in.length == 0)
            return status(HttpStatus.NO_CONTENT).build();
        //Save the Cover URL to DB
        book.setCoverUrl(callOpenLibrary.getUrlforIsbnMedium(book.getIsbn()));
        Book booksaved= bookServiceImpl.save(book);
        booksaved.getCoverUrl();
        return new ResponseEntity<>(in, headers, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable long id) {
        try {
            return status(HttpStatus.OK).body(bookServiceImpl.findById(id));
        } catch (BookNotFoundException e) {
            return status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        try {
            return status(HttpStatus.CREATED).body(bookServiceImpl.save(book));
        } catch (IllegalArgumentException e) {
            return status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable long id, @RequestBody Book book) {
        try {
            return status(HttpStatus.OK).body(bookServiceImpl.update(id, book));
        } catch (BookNotFoundException e) {
            return status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBook(@PathVariable long id) {
        try {
            bookServiceImpl.deleteById(id);
            return status(HttpStatus.NO_CONTENT).build();
        } catch (BookNotFoundException e) {
            return status(HttpStatus.NOT_FOUND).build();
        }
    }

    public void setBookService(BookServiceImpl bookServiceImpl) {
        this.bookServiceImpl = bookServiceImpl;
    }
}
