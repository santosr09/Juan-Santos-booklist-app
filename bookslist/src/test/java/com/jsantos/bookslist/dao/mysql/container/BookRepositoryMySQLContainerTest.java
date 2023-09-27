package com.jsantos.bookslist.dao.mysql.container;


import com.jsantos.bookslist.dao.BookRepository;
import com.jsantos.bookslist.model.Book;
import com.jsantos.bookslist.model.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("container")
@Tag("ContainerTest")
@Sql({"classpath:schema-mysql.sql"})
class BookRepositoryMySQLContainerTest extends BaseClassContainers {

    @Autowired
    private BookRepository repository;

    private long bookA_Id;
    private Set<Category> categories = new HashSet<>();

    @BeforeEach
    private void setUp() {
        Book bookA = new Book();
        bookA.setName("Spring boot");
        bookA_Id = (repository.save(bookA)).getId();
    }

    @Test
    public void saveBook() {
        Book newBook = new Book();
        newBook.setName("New book");
        long newBook_Id = (repository.save(newBook)).getId();

        Optional<Book> found = repository.findById(newBook_Id);
        Assertions.assertTrue("New book".equals(found.get().getName()));
    }

    @Test
    public void saveBookAndCategory() {
        Book newBook = new Book();
        newBook.setName("New book with category");

        Category newCategory = new Category();
        newCategory.setName("New Category");
        categories.add(newCategory);
        newBook.setCategories(categories);

        long newBook_Id = (repository.save(newBook)).getId();

        Optional<Book> found = repository.findById(newBook_Id);

        Assertions.assertTrue("New book with category".equals(repository.findById(newBook_Id).get().getName()));
        Assertions.assertTrue(found.get().getCategories().contains(newCategory));
    }

    //@Test
    public void findyBookById() {
        Optional<Book> found = repository.findById(bookA_Id);

        Assertions.assertTrue(found.get().getName().equals("Spring boot"));
    }

    //@Test
    public void updateObjectBook() {
        Optional<Book> found = repository.findById(bookA_Id);

        if(found.isPresent()) {
            found.get().setName("Book updated");
            repository.save(found.get());
        }
        Assertions.assertTrue("Book updated".equals(repository.findById(bookA_Id).get().getName()));
    }

    //@Test
    public void deleteObjectBook() {
        Book bookB = new Book();
        bookB.setName("Spring boot delete");
        long bookB_Id = (repository.save(bookB)).getId();

        repository.delete(bookB);
        Assertions.assertTrue(repository.findById(bookB_Id).isEmpty());
    }

    //@Test
    public void deleteBookById() {
        Book bookC = new Book();
        bookC.setName("Spring boot delete");
        long bookC_Id = (repository.save(bookC)).getId();

        repository.deleteById(bookC_Id);
        Assertions.assertTrue(repository.findById(bookC_Id).isEmpty());
    }

}