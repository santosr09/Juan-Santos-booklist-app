package com.jsantos.bookslist.service;

import com.jsantos.bookslist.dao.BookRepository;
import com.jsantos.bookslist.model.Book;
import com.jsantos.bookslist.model.Category;
import com.jsantos.bookslist.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
@Tag("UnitTest")
class BookServiceTest {

    private BookServiceImpl service;
    @Mock
    private Book mockBook;
    @Mock
    private BookRepository repositoryMock;
    @Captor
    private ArgumentCaptor<Book> bookArgumentCaptor;


    @BeforeEach
    private void setUp() {
        service = new BookServiceImpl();
        service.setRepository(repositoryMock);
    }

    @Test
    public void saveBook() {
        Mockito.when(mockBook.getName()).thenReturn("Spring book");
        Mockito.when(repositoryMock.save(mockBook)).thenReturn(mockBook);

        service.save(mockBook);
        Mockito.verify(repositoryMock, Mockito.times(1)).save(bookArgumentCaptor.capture());
        Assertions.assertThat(bookArgumentCaptor.getValue().getName()).isEqualTo("Spring book");
    }

    @Test
    public void updateBookById() {
        Mockito.when(mockBook.getName()).thenReturn("Book updated");
        Mockito.when(repositoryMock.save(mockBook)).thenReturn(mockBook);

        service.save(mockBook);
        Mockito.verify(repositoryMock, Mockito.times(1)).save(bookArgumentCaptor.capture());
        Assertions.assertThat(bookArgumentCaptor.getValue().getName()).isEqualTo("Book updated");
    }

    @Test
    public void getBooksByCategory() {
        ArrayList<Book> books = new ArrayList<>();
        books.add(mockBook);
        Set<Category> categories = new HashSet<>();
        Category mockCategory = Mockito.mock(Category.class, "mockCategory");
        categories.add(mockCategory);
        Mockito.when(mockBook.getCategories()).thenReturn(categories);
        Mockito.when(repositoryMock.findAll()).thenReturn(books);

        Iterable<Book> result =  service.getBooksByCategory(mockCategory);
        Assertions.assertThat(result.iterator().next()).isEqualTo(mockBook);

    }

}