package com.jsantos.bookslist.dao.mysql;

import com.jsantos.bookslist.dao.BookRepository;
import com.jsantos.bookslist.model.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("local")
@Tag("mysql")
class BookRepositoryMySQLTest {

    @Autowired
    private BookRepository repository;

    @Test
    public void getAllBooks() {
        Iterable<Book> books = repository.findAll();
        Assertions.assertThat(books).isNotEmpty();
    }

}