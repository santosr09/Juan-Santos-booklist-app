package com.jsantos.bookslist.service;

import com.jsantos.bookslist.dao.BookRepository;
import com.jsantos.bookslist.dao.UserRepository;
import com.jsantos.bookslist.model.Book;
import com.jsantos.bookslist.model.User;
import com.jsantos.bookslist.model.exception.BookNotFoundException;
import com.jsantos.bookslist.model.exception.UserNotFoundException;
import com.jsantos.bookslist.service.impl.UserServiceImpl;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@Tag("UnitTest")
class UserServiceTest {

    private UserServiceImpl service;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private User mockUser;
    @Mock
    private Book mockBook;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @BeforeEach
    private void setUp() {
        service = new UserServiceImpl();
        service.setUserRepository(userRepository);
        service.setBookRepository(bookRepository);
        service.setPasswordEncoder(passwordEncoder);
    }

    @Test
    void save() {
        Mockito.when(mockUser.getFirstName()).thenReturn("Sanchez");
        Mockito.when(userRepository.save(mockUser)).thenReturn(mockUser);

        service.save(mockUser);
        Mockito.verify(userRepository, Mockito.times(1)).save(userArgumentCaptor.capture());
        Assertions.assertThat(userArgumentCaptor.getValue().getFirstName()).isEqualTo("Sanchez");
    }

    @Test
    void update() throws UserNotFoundException {
        Mockito.when(mockUser.getFirstName()).thenReturn("Hill");
        Mockito.when(userRepository.findById(0L)).thenReturn(Optional.of(mockUser));
        Mockito.when(userRepository.save(mockUser)).thenReturn(mockUser);

        service.update(0L, mockUser);
        Mockito.verify(userRepository, Mockito.times(1)).save(userArgumentCaptor.capture());
        Assertions.assertThat(userArgumentCaptor.getValue().getFirstName()).isEqualTo("Hill");
    }

    @Test
    public void assignBookToUser() throws BookNotFoundException, UserNotFoundException {
        Mockito.when(bookRepository.findById(0L)).thenReturn(Optional.of(mockBook));
        Mockito.when(userRepository.findById(0L)).thenReturn(Optional.of(mockUser));
        Mockito.when(mockUser.getBooks()).thenReturn(new HashSet<>());
        Mockito.when(userRepository.save(mockUser)).thenReturn(mockUser);

        service.assignBook(0L, 0L);
        Mockito.verify(userRepository, Mockito.times(1)).save(userArgumentCaptor.capture());
        Assertions.assertThat(userArgumentCaptor.getValue().getBooks()).contains(mockBook);
    }

    @Test
    public void assignBookUnsavedToUser() {
        Assertions.assertThatThrownBy(() -> service.assignBook(0L, 0L))
                .isInstanceOf(Exception.class);
    }

    @Test
    void deleteById() throws UserNotFoundException {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        service.deleteById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1L);
    }

}