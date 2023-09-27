package com.jsantos.bookslist.dao;

import com.jsantos.bookslist.model.Book;
import com.jsantos.bookslist.model.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@DataJpaTest
@Tag("UnitTest")
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repository;

    private long categoryA_Id;
    private Set<Book> books = new HashSet<>();

    @BeforeEach
    private void setUp() {
        Category categoryA = new Category();
        categoryA.setName("Category A");

        Book bookA = new Book();
        bookA.setName("Book A");
        books.add(bookA);

        Book bookB = new Book();
        bookB.setName("Book B");
        books.add(bookB);

        categoryA.setBooks(books);
        categoryA_Id = repository.save(categoryA).getId();

    }

    @Test
    public void saveCategory() {
        Category newCategory = new Category();
        newCategory.setName("New Category");

        long newId = repository.save(newCategory).getId();
        Optional<Category> found = repository.findById(newId);
        Assertions.assertEquals("New Category", found.get().getName());
    }

    @Test
    public void saveCategoryAndBook() {
        Category newCategory = new Category();
        newCategory.setName("New Category with Books");

        Book bookC = new Book();
        bookC.setName("Book C");
        books.add(bookC);

        Set<Book> newBooks = new HashSet<>();
        newBooks.add(bookC);
        newCategory.setBooks(newBooks);

        long newId = repository.save(newCategory).getId();
        Optional<Category> found = repository.findById(newId);

        Assertions.assertEquals("New Category with Books", found.get().getName());
        Assertions.assertTrue(found.get().getBooks().contains(bookC));
    }

    @Test
    public void updateCategory() {
        Optional<Category> found = repository.findById(categoryA_Id);

        if (found.isPresent())
            found.get().setName("Category updated");

        repository.save(found.get());
        Assertions.assertEquals( "Category updated", repository.findById(categoryA_Id).get().getName());
    }

    @Test
    public void deleteObjectCategory() {
        Category categoryB = new Category();
        categoryB.setName("Category B");
        long categoryB_Id = repository.save(categoryB).getId();

        repository.delete(categoryB);
        Assertions.assertTrue(repository.findById(categoryB_Id).isEmpty());
    }

    @Test
    public void deleteCategoryById() {
        Category categoryC = new Category();
        categoryC.setName("Category C");
        long categoryC_Id = repository.save(categoryC).getId();

        repository.deleteById(categoryC_Id);
        Assertions.assertTrue(repository.findById(categoryC_Id).isEmpty());

    }

    @Test
    public void existsByNameIgnoreCase() {
        Optional<Category> categoryC = repository.findById(categoryA_Id);
        categoryC.get().setName("Category XYZ");
        repository.save(categoryC.get());

        Assertions.assertTrue(repository.existsByNameIgnoreCase("Category XYZ"));
    }

}
