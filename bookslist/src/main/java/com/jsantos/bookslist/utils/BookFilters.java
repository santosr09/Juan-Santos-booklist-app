package com.jsantos.bookslist.utils;

public enum BookFilters {

    AUTHOR ("Author"),
    CURRENT_YEAR("CurrentYear"),
    CATEGORY("Category"),
    OLDEST_BOOK("OldestBook");

    private String filterName;

    BookFilters(String name) {
        this.filterName = name;
    }

    public String getFilterName() {
        return filterName;
    }

}
