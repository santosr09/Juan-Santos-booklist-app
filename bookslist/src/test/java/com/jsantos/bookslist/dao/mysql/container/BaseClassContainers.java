package com.jsantos.bookslist.dao.mysql.container;

import org.testcontainers.containers.MySQLContainer;

public class BaseClassContainers {

    static MySQLContainer mySQLContainer = (MySQLContainer) new MySQLContainer("mysql:latest")
            .withDatabaseName("test_books")
            .withUsername("testuser")
            .withPassword("pass");

    static {
        mySQLContainer.start();
    }

}
