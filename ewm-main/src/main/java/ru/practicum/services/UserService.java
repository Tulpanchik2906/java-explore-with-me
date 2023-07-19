package ru.practicum.services;

import ru.practicum.model.User;

import java.util.List;

public interface UserService {
    User create(User user);

    void delete(Long userId);

    List<User> findAll(List<Long> ids, Integer from, Integer size);
}
