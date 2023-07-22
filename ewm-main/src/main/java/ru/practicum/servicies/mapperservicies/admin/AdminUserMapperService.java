package ru.practicum.servicies.mapperservicies.admin;

import ru.practicum.NewUserRequest;
import ru.practicum.UserDto;

import java.util.List;

public interface AdminUserMapperService {

    UserDto addUser(NewUserRequest user);

    void deleteUserById(Long id);

    List<UserDto> findAll(List<Long> ids, Integer from, Integer size);
}
