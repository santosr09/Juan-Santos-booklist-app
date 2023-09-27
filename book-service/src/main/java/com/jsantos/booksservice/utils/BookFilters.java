package com.jsantos.booksservice.utils;

public enum BookFilters {

    AUTHOR ("Author"),
    CURRENT_YEAR("CurrentYear"),
    CATEGORY("Category"),
    OLDEST_BOOK("OldestBook"),
    USER_ID("User_ID");

    private String filterName;

    BookFilters(String name) {
        this.filterName = name;
    }

    public String getFilterName() {
        return filterName;
    }

}
