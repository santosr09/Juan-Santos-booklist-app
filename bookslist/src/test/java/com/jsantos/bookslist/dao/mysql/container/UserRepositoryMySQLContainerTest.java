package com.jsantos.bookslist.dao.mysql.container;

import com.jsantos.bookslist.dao.UserRepository;
import com.jsantos.bookslist.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("container")
@Tag("ContainerTest")
class UserRepositoryMySQLContainerTest {

    @Autowired
    private UserRepository repository;
    long userA_Id;

    @BeforeEach
    private void setUp() {
        User userA = new User();
        userA.setFirstName("Juan");
        userA.setLastName("Pérez");
        userA.setUsername("jperez");
        userA.setPassword("12348765");

        userA_Id = repository.save(userA).getId();
    }

    @Test
    public void saveUser() {
        User userB = new User();
        userB.setFirstName("Pedro");
        userB.setLastName("Juárez");
        userB.setUsername("pjuarez");
        userB.setPassword("87651234");

        long userB_Id = repository.save(userB).getId();

        Optional<User> found = repository.findById(userB_Id);
        Assertions.assertTrue(found.isPresent());

    }

    @Test
    public void updateUser() {
        Optional<User> found = repository.findById(userA_Id);

        if(found.isPresent())
            found.get().setFirstName("Jose");

        repository.save(found.get());
        Assertions.assertEquals("Jose", repository.findById(userA_Id).get().getFirstName());
    }

    @Test
    public void deleteObjectUser() {
        User newUser = new User();
        newUser.setFirstName("User");
        newUser.setLastName("X");
        long newUser_Id = repository.save(newUser).getId();

        repository.delete(newUser);
        Assertions.assertTrue(repository.findById(newUser_Id).isEmpty());
    }

    @Test
    public void deleteUserById() {
        User newUser = new User();
        newUser.setFirstName("User");
        newUser.setLastName("X");
        long newUser_Id = repository.save(newUser).getId();

        repository.deleteById(newUser_Id);
        Assertions.assertTrue(repository.findById(newUser_Id).isEmpty());
    }

}