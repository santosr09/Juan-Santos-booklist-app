package com.jsantos.booksservice.model.mappers;

import com.jsantos.booksservice.model.Book;
import com.jsantos.booksservice.model.dto.BookDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    Book dtoToEntity(BookDTO dto);
    BookDTO entityToDto(Book entity);

    Iterable<BookDTO> listOfEntitiesToListOfDtos(Iterable<Book> books);
    Iterable<Book> listOfDtosToListOfEntities(Iterable<BookDTO> books);

    Book updateBookFromDto(BookDTO dto, @MappingTarget Book book);

}
