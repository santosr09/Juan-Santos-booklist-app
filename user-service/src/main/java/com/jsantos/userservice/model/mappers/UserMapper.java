package com.jsantos.userservice.model.mappers;

import com.jsantos.userservice.model.User;
import com.jsantos.userservice.model.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User dtoToEntity(UserDTO dto);

    UserDTO entityToDto(User entity);

    Iterable<UserDTO> listOfEntitiesToListOfDtos(Iterable<User> users);

    Iterable<User> listOfDtosToListOfEntities(Iterable<UserDTO> users);

    User updateUserFromDto(UserDTO dto, @MappingTarget User user);

}
