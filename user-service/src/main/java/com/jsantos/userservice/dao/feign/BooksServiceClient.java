package com.jsantos.userservice.dao.feign;

import com.jsantos.userservice.model.dto.BookDTO;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Map;

@FeignClient(name="book-service")
public interface BooksServiceClient {

    @GetMapping("/books")
    @CircuitBreaker(name="book-service", fallbackMethod = "getBooksFallback")
    public ResponseEntity<? extends Object> getBooks(@RequestParam Map<String, String> queryParams, @RequestHeader HttpHeaders headers);

    default ResponseEntity<? extends Object> getBooksFallback(@RequestParam Map<String, String> queryParams, @RequestHeader HttpHeaders headers, Throwable exception) {
        Iterable<BookDTO> booksEmpty = new ArrayList<>();
        return new ResponseEntity<Iterable<BookDTO>>(booksEmpty, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/books/{bookId}/covers")
    @CircuitBreaker(name="book-service", fallbackMethod = "getBooksCoverFallback")
    public ResponseEntity<byte[]> getCover(@PathVariable String bookId, @RequestHeader HttpHeaders headers);

    default ResponseEntity<byte[]> getBooksCoverFallback(@PathVariable String bookId, @RequestHeader HttpHeaders headers) {
        byte [] bytesEmpty = new byte[]{};
        return new ResponseEntity<byte[]>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/books/{bookID}")
    @CircuitBreaker(name="book-service", fallbackMethod = "getBookFallback")
    public ResponseEntity<BookDTO> getBook(@PathVariable String bookID);

    default ResponseEntity<BookDTO> getBookFallback(@PathVariable String bookID) {
        return new ResponseEntity<BookDTO>(HttpStatus.SERVICE_UNAVAILABLE);
    }

}
