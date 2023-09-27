package com.jsantos.bookslist.dao;

import com.jsantos.bookslist.model.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    boolean existsByNameIgnoreCase(String name);

    Category findByName(String name);
}
