package com.jsantos.bookslist.service;

import com.jsantos.bookslist.dao.BookRepository;
import com.jsantos.bookslist.dao.CategoryRepository;
import com.jsantos.bookslist.model.Book;
import com.jsantos.bookslist.model.Category;
import com.jsantos.bookslist.model.exception.BookNotFoundException;
import com.jsantos.bookslist.model.exception.CategoryNotFoundException;
import com.jsantos.bookslist.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.assertj.core.api.Assertions;

import java.util.HashSet;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@Tag("UnitTest")
class CategoryServiceTest {

    private CategoryServiceImpl service;
    @Mock
    private Book mockBook;
    @Mock
    private Category mockCategory;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookRepository bookRepository;
    @Captor
    private ArgumentCaptor<Category> categoryArgumentCaptor;
    @Captor
    private ArgumentCaptor<Book> bookArgumentCaptor;

    @BeforeEach
    public void setUp() {
        service = new CategoryServiceImpl();
        service.setRepository(categoryRepository);
        service.setBookRepository(bookRepository);
    }

    @Test
    void save() {
        Mockito.when(mockCategory.getName()).thenReturn("New Category");
        Mockito.when(categoryRepository.save(mockCategory)).thenReturn(mockCategory);

        service.save(mockCategory);
        Mockito.verify(categoryRepository, Mockito.times(1)).save(categoryArgumentCaptor.capture());
        Assertions.assertThat(categoryArgumentCaptor.getValue().getName()).isEqualTo("New Category");
    }

    @Test
    void update() {
        Mockito.when(mockCategory.getName()).thenReturn("Category updated");

        service.save(mockCategory);
        Mockito.verify(categoryRepository, Mockito.times(1)).save(categoryArgumentCaptor.capture());
        Assertions.assertThat(categoryArgumentCaptor.getValue().getName()).isEqualTo("Category updated");
    }

    @Test
    void assignToBook() throws CategoryNotFoundException, BookNotFoundException {
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(mockCategory));
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));
        Mockito.when(mockCategory.getBooks()).thenReturn(new HashSet<>());

        service.assignToBook(1L, 1L);
        Mockito.verify(categoryRepository, Mockito.times(1))
                .save(categoryArgumentCaptor.capture());

        Assertions.assertThat(categoryArgumentCaptor.getValue().getBooks()).contains(mockBook);
    }

    @Test
    void assignCategoryToBookNotSaved() throws CategoryNotFoundException {
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(mockCategory));

        Assertions.assertThatThrownBy(() -> service.assignToBook(1L, 1L)).isInstanceOf(BookNotFoundException.class);

        Mockito.verify(bookRepository, Mockito.times(1)).findById(1L);
    }

}