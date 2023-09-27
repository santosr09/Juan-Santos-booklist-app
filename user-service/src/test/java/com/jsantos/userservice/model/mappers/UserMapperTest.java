package com.jsantos.userservice.model.mappers;

import com.jsantos.userservice.model.Book;
import com.jsantos.userservice.model.Rol;
import com.jsantos.userservice.model.Token;
import com.jsantos.userservice.model.User;
import com.jsantos.userservice.model.dto.BookDTO;
import com.jsantos.userservice.model.dto.RolDTO;
import com.jsantos.userservice.model.dto.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class UserMapperTest {

    @Test
    public void dtoToEntityTest() {
        Set<BookDTO> books = new HashSet<>();
        Set<RolDTO> roles = new HashSet<>();
        UserDTO dto = new UserDTO("123abc", "Anonimo", "Segundo", "USA", "12-01-2022", "anseg", "123456", books, roles);
        User user = UserMapper.INSTANCE.dtoToEntity(dto);

        assertThat(user).isNotNull();
        assertThat(user.getFirstName()).isEqualTo("Anonimo");
        assertThat(user.getLastName()).isEqualTo("Segundo");
        assertThat(user.getRegistrationDate()).isEqualTo("12-01-2022");

    }

    @Test
    public void entityToDto() {
        Set<Book> books = new HashSet<>();
        Set<Rol> roles = new HashSet<>();
        Token token = new Token();
        User user = new User(1L, "123abc", "Anonimo", "Segundo", "USA", "12-01-2022", "anseg", "123456", books, roles, token);

        UserDTO dto = UserMapper.INSTANCE.entityToDto(user);

        assertThat(dto).isNotNull();
        assertThat(dto.getUserId()).isEqualTo("123abc");

    }

    @Test
    public void entityListToDtoList() {
        Set<BookDTO> books = new HashSet<>();
        Set<RolDTO> roles = new HashSet<>();
        UserDTO dto = new UserDTO("123abc", "Anonimo", "Segundo", "USA", "12-01-2022", "anseg", "123456", books, roles);
        UserDTO dto2 = new UserDTO("321abc", "Anonimo2", "Segundo2", "USA", "12-01-2022", "anseg2", "123456", books, roles);
        List<UserDTO> userDTOS = new ArrayList<>();
        userDTOS.add(dto);
        userDTOS.add(dto2);

        List<User> users = (List<User>) UserMapper.INSTANCE.listOfDtosToListOfEntities(userDTOS);

        assertThat(users).isNotNull();
        assertThat(users.get(1).getUserId()).isEqualTo("321abc");
    }

    @Test
    public void dtoListToEntityList() {
        Set<Book> books = new HashSet<>();
        Set<Rol> roles = new HashSet<>();
        Token token = new Token();
        User entity = new User(1L, "123abc", "Anonimo", "Segundo", "USA", "12-01-2022", "anseg", "123456", books, roles, token);
        User entity2 = new User(2L, "321abc", "Anonimo2", "Segundo2", "USA", "12-01-2022", "anseg2", "123456", books, roles, token);
        List<User> users = new ArrayList<>();
        users.add(entity);
        users.add(entity2);

        List<UserDTO> dtos = (List<UserDTO>) UserMapper.INSTANCE.listOfEntitiesToListOfDtos(users);

        assertThat(dtos).isNotNull();
        assertThat(dtos.get(1).getUserId()).isEqualTo("321abc");
    }

}