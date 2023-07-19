package ru.practicum.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.User;
import ru.practicum.storage.UserRepository;
import ru.practicum.util.PageUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(Long userId) {
        get(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public List<User> findAll(List<Long> ids, Integer from, Integer size) {
        if (ids == null) {
            return findAllWithOutIds(from, size);
        } else {
            return findAllByIds(ids, from, size);
        }
    }

    private List<User> findAllByIds(List<Long> ids, int from, int size) {
        int startPage = PageUtil.getStartPage(from, size);

        if (PageUtil.isTwoSite(from, size)) {
            List<User> list = userRepository.findByIdIn(ids, PageRequest.of(startPage, size));
            list.addAll(userRepository.findByIdIn(ids, PageRequest.of(startPage + 1, size)));
            return PageUtil.getPageListForTwoPage(list,
                    PageUtil.getStartFrom(from, size), size);
        } else {
            return userRepository.findByIdIn(ids, PageRequest.of(startPage, size))
                    .stream().limit(size)
                    .collect(Collectors.toList());
        }
    }

    private List<User> findAllWithOutIds(int from, int size) {
        int startPage = PageUtil.getStartPage(from, size);

        if (PageUtil.isTwoSite(from, size)) {
            List<User> list = userRepository.findAll(PageRequest.of(startPage, size))
                    .stream().collect(Collectors.toList());
            list.addAll(userRepository.findAll(PageRequest.of(startPage + 1, size))
                    .stream().collect(Collectors.toList()));
            return PageUtil.getPageListForTwoPage(list,
                    PageUtil.getStartFrom(from, size), size);
        } else {
            return userRepository.findAll(PageRequest.of(startPage, size))
                    .stream().limit(size)
                    .collect(Collectors.toList());
        }
    }

    private User get(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Не найден пользователь с id: " + userId));
    }
}
