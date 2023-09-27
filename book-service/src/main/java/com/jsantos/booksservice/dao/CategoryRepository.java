package com.jsantos.booksservice.dao;

import com.jsantos.booksservice.model.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    boolean existsByNameIgnoreCase(String name);

    Category findByName(String name);
}
