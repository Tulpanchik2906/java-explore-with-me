package ru.practicum.servicies.mapperservicies;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDto;
import ru.practicum.mappers.UserMapper;
import ru.practicum.servicies.logicservicies.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserMapperServiceImpl implements AdminUserMapperService {

    private final UserService userService;

    @Override
    public UserDto addUser(NewUserRequest user) {
        return UserMapper.INSTANCE.toUserDto(
                userService.create(UserMapper.INSTANCE.toUser(user)));
    }

    @Override
    public void deleteUserById(Long id) {
        userService.delete(id);
    }

    @Override
    public List<UserDto> findAll(
            List<Long> ids, Integer from, Integer size) {
        return userService.findAll(ids, from, size).stream()
                .map(UserMapper.INSTANCE::toUserDto)
                .collect(Collectors.toList());
    }
}
