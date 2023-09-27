package com.jsantos.booksservice.dao;

import com.jsantos.booksservice.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Long> {

    void deleteByBookId(String bookId);
    Optional<Book> findByBookId(String bookdId);

    boolean existsByNameAndAuthorAllIgnoreCase(String name, String author);

    Iterable<Book> findByAuthor(String author);

    @Query("SELECT b FROM Book b WHERE YEAR(b.publishedDate) = :published_year")
    Iterable<Book> findByPublishedYear(@Param("published_year") int published_year);

    @Query(value = "SELECT books.* FROM books INNER JOIN users_books ON users_books.book_id=books.id AND users_books.user_id =(SELECT id FROM users WHERE user_id=:userID)", nativeQuery = true)
    Iterable<Book> findByUser(@Param("userID") String userID);
}

