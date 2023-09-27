package com.jsantos.bookslist.controller;

import com.jsantos.bookslist.model.Book;
import com.jsantos.bookslist.service.impl.BookServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Arrays.asList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers =  BookController.class)
class BookControllerTest {

    @MockBean
    private BookServiceImpl bookServiceImpl;
    @Mock
    private Book mockBook;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getBooks() throws Exception {
        Book book = new Book();
        book.setName("new book");
        Mockito.when(bookServiceImpl.findAll()).thenReturn(asList(book));

        mockMvc.perform(get("/books"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.size()", Matchers.is(1)));
    }

    @Test
    void getBookById() throws Exception {
        Mockito.when(bookServiceImpl.findById(1L)).thenReturn(mockBook);

        mockMvc.perform(get("/books"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.size()", Matchers.is(0)));
    }

}