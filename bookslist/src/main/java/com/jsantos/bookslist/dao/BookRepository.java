package com.jsantos.bookslist.dao;

import com.jsantos.bookslist.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends CrudRepository<Book, Long> {

    boolean existsByNameAndAuthorAllIgnoreCase(String name, String author);

    Iterable<Book> findByAuthor(String author);

    @Query("SELECT b FROM Book b WHERE YEAR(b.publishedDate) = :published_year")
    Iterable<Book> findByPublishedYear(@Param("published_year") int published_year);
}
