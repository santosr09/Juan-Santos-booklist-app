package com.jsantos.booksservice.service.impl;

import com.jsantos.booksservice.dao.BookRepository;
import com.jsantos.booksservice.model.Book;
import com.jsantos.booksservice.model.Category;
import com.jsantos.booksservice.model.dto.BookDTO;
import com.jsantos.booksservice.model.exception.BookNotFoundException;
import com.jsantos.booksservice.model.mappers.BookMapper;
import com.jsantos.booksservice.service.BookService;
import com.jsantos.booksservice.utils.Messages;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@Setter
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository repository;

    @Autowired
    private RestTemplate restTemplateUser;

    @Autowired
    private RestTemplate restTemplateCovers;

    @Override
    public Iterable<BookDTO> findAll() {
        return BookMapper.INSTANCE.listOfEntitiesToListOfDtos(repository.findAll());
    }

    @Override
    public BookDTO findByBookId(String bookId) throws BookNotFoundException {
        Book entity = repository.findByBookId(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
        return BookMapper.INSTANCE.entityToDto(entity);
    }

    @Override
    public Iterable<BookDTO> findByUser(String userID) {
        return BookMapper.INSTANCE.listOfEntitiesToListOfDtos(repository.findByUser(userID));
    }

    @Override
    public Iterable<BookDTO> getAllBooksByAuthor(String author) {
        Stream<Book> entities = StreamSupport.stream(repository.findAll().spliterator(), false);
        List<Book> books = entities.filter(b -> author.equals(b.getAuthor()))
                .collect(Collectors.toList());
        return BookMapper.INSTANCE.listOfEntitiesToListOfDtos(books);
    }

    @Override
    public Iterable<BookDTO> getBooksPublishedInCurrentYear() {
        LocalDate currentDate = LocalDate.now();
        Iterable<Book> entities = StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(book -> book.getPublishedDate().getYear() == currentDate.getYear())
                .collect(Collectors.toList());
        return BookMapper.INSTANCE.listOfEntitiesToListOfDtos(entities);
    }

    @Override
    public Iterable<BookDTO> getAllBooksByPublishedYear(String year) {
        LocalDate currentDate = LocalDate.now();
        if(Integer.parseInt(year) > currentDate.getYear())
            throw new RuntimeException(Messages.getMessagesForKey("error.notavalidyear"));
        return BookMapper.INSTANCE.listOfEntitiesToListOfDtos(repository.findByPublishedYear(Integer.parseInt(year)));
    }

    @Override
    public Iterable<BookDTO> getBooksByCategory(Category category) {
        Stream<Book> entities = StreamSupport.stream(repository.findAll().spliterator(), false);
        List<Book> books = entities.filter(b -> b.getCategories().contains(category))
                .collect(Collectors.toList());
        return BookMapper.INSTANCE.listOfEntitiesToListOfDtos(books);
    }

    public Optional<BookDTO> getOldestBook() {
        Optional<Book> book = StreamSupport.stream(repository.findAll().spliterator(), false)
                .min(Comparator.comparing(Book::getPublishedDate));
        return Optional.ofNullable(BookMapper.INSTANCE.entityToDto(book.get()));
    }

    @Override
    public BookDTO save(BookDTO book) {

        Book entity = BookMapper.INSTANCE.dtoToEntity(book);
        entity.setBookId(UUID.randomUUID().toString());
        return BookMapper.INSTANCE.entityToDto(repository.save(entity));
    }

    @Override
    public BookDTO update(String bookId, BookDTO book) throws BookNotFoundException {
        Optional<Book> entity = repository.findByBookId(bookId);
        if (entity.isPresent()){
            Book savedEntity = BookMapper.INSTANCE.updateBookFromDto(book, entity.get());
            savedEntity = repository.save(savedEntity);
            return BookMapper.INSTANCE.entityToDto(savedEntity);
        }
        throw new BookNotFoundException(bookId);
    }

    @Transactional
    @Override
    public void deleteByBookId(String bookId) throws BookNotFoundException {
        this.findByBookId(bookId);
        repository.deleteByBookId(bookId);
    }


/*    public byte[] getCover(String bookId) throws BookNotFoundException {
        BookDTO dto;
        try {
            dto = this.findByBookId(bookId);
        } catch (BookNotFoundException e) {
            throw new BookNotFoundException(bookId);
        }
        StringBuilder urlStr = new StringBuilder("/");

        urlStr
                .append(dto.getIsbn())
                .append("-")
                .append("M")
                .append(".jpg");

        byte[] in = restTemplateCovers.getForObject(urlStr.toString(), byte[].class);

        return in;
    }*/

    public void setRepository(BookRepository repository) {
        this.repository = repository;
    }

}