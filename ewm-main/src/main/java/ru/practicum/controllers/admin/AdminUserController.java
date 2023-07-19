package ru.practicum.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDto;
import ru.practicum.mappers.UserMapper;
import ru.practicum.services.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminUserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Valid @RequestBody NewUserRequest user) {
        log.info("Получен запрос на создание нового пользователя.");
        return userMapper.toUserDto(userService.create(userMapper.toUser(user)));
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long id) {
        log.info("Получен запрос на удаление пользователя с id: {} .", id);
        userService.delete(id);
    }

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> findAll(@RequestParam(required = false) List<Long> ids,
                        @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                        @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос на просмотр списка пользователей");
       return userService.findAll(ids, from, size).stream()
               .map(x -> userMapper.toUserDto(x))
               .collect(Collectors.toList());
    }
}
