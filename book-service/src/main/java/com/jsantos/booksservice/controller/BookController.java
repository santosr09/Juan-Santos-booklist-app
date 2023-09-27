package com.jsantos.booksservice.controller;

import com.jsantos.booksservice.model.Book;
import com.jsantos.booksservice.model.dto.BookDTO;
import com.jsantos.booksservice.model.exception.BookNotFoundException;
import com.jsantos.booksservice.model.mappers.BookMapper;
import com.jsantos.booksservice.service.BookService;
import com.jsantos.booksservice.service.CategoryService;
import com.jsantos.booksservice.utils.BookFilters;
import com.jsantos.booksservice.utils.CallOpenLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.jsantos.booksservice.utils.BookFilters.AUTHOR;
import static com.jsantos.booksservice.utils.BookFilters.USER_ID;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookServiceImpl;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    CallOpenLibrary callOpenLibrary;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<? extends Object> getBooks(@RequestParam Map<String, String> queryParams) {
        if(!queryParams.isEmpty()){
            return getBooksFiltered(queryParams);
        }

        //Without filters
        Iterable<BookDTO> books = bookServiceImpl.findAll();
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
                case USER_ID:
                    return getBooksFromUser(params.get(USER_ID.getFilterName()));
                default:
                    return status(HttpStatus.NO_CONTENT).build();
            }
        }
        return status(HttpStatus.NO_CONTENT).build();
    }

    private ResponseEntity<Iterable<BookDTO>> getBooksPublishedInCurrentYear() {
        Iterable<BookDTO> books = bookServiceImpl.getBooksPublishedInCurrentYear();
        if(StreamSupport.stream(books.spliterator(), false).findAny().isEmpty()){
            return status(HttpStatus.NO_CONTENT).build();
        }
        return status(HttpStatus.OK).body(books);
    }

    private ResponseEntity<Iterable<BookDTO>> getBooksByAuthor(String author) {
        Iterable<BookDTO> books = bookServiceImpl.getAllBooksByAuthor(author);
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

    private ResponseEntity<BookDTO> getOldestBook() {
        Optional<BookDTO> book = bookServiceImpl.getOldestBook();
        if(book.isPresent())
            return status(HttpStatus.OK).body(book.get());
        return status(HttpStatus.NO_CONTENT).build();
    }

    private ResponseEntity<List<BookDTO>> getBooksFromUser(String userID) {
        List<BookDTO> books = (List<BookDTO>) bookServiceImpl.findByUser(userID);
        if(!books.isEmpty())
            return status(HttpStatus.OK).body(books);
        return status(HttpStatus.NO_CONTENT).body(books);
    }

    @GetMapping("/{bookId}/covers")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<byte[]> getCover(@PathVariable String bookId) {
        HttpHeaders headers = new HttpHeaders();
        BookDTO book;
        try {
            book = bookServiceImpl.findByBookId(bookId);
        } catch (BookNotFoundException e) {
            return status(HttpStatus.NO_CONTENT).build();
        }
        headers.setContentType(MediaType.IMAGE_JPEG);
        byte[] in = callOpenLibrary.getImageByIsbnMedium(book.getIsbn());

        if( in == null || in.length == 0)
            return status(HttpStatus.NO_CONTENT).build();
        //Save the Cover URL to DB
        book.setCoverUrl(callOpenLibrary.getUrlforIsbnMedium(book.getIsbn()));
        BookDTO booksaved= null;
        try {
            booksaved = bookServiceImpl.update(bookId, book);
        } catch (BookNotFoundException e) {
            return status(HttpStatus.NOT_FOUND).build();
        }
        booksaved.getCoverUrl();
        return new ResponseEntity<>(in, headers, HttpStatus.OK);

    }

    @GetMapping("/{bookID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookDTO> getBook(@PathVariable String bookID) {
        try {
            return status(HttpStatus.OK).body(bookServiceImpl.findByBookId(bookID));
        } catch (BookNotFoundException e) {
            return status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO book) {
        try {
            return status(HttpStatus.CREATED).body(bookServiceImpl.save(book));
        } catch (IllegalArgumentException e) {
            return status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookDTO> updateBook(@PathVariable String id, @RequestBody BookDTO book) {
        try {
            return status(HttpStatus.OK).body(bookServiceImpl.update(id, book));
        } catch (BookNotFoundException e) {
            return status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity deleteBook(@PathVariable String id) {
        try {
            bookServiceImpl.deleteByBookId(id);
            return status(HttpStatus.NO_CONTENT).build();
        } catch (BookNotFoundException e) {
            return status(HttpStatus.NOT_FOUND).build();
        }
    }

/*    @GetMapping("/{bookId}/covers")
    public ResponseEntity<byte[]> getCover(@PathVariable String bookId){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        byte[] bytes = new byte[0];
        try {
            bytes = bookServiceImpl.getCover(bookId);
        } catch (BookNotFoundException e) {
            return status(HttpStatus.NOT_FOUND).build();
        }
        if(bytes.length != 0)
            return status(HttpStatus.OK).body(bytes);
        return status(HttpStatus.NO_CONTENT).body(bytes);
    }*/

    public void setBookService(BookService bookServiceImpl) {
        this.bookServiceImpl = bookServiceImpl;
    }
}
