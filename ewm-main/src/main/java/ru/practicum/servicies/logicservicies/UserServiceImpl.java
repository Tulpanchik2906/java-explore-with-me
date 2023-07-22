package ru.practicum.servicies.logicservicies;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        get(userId);
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll(List<Long> ids, Integer from, Integer size) {
        if (ids == null) {
            return findAllWithOutIds(from, size);
        } else {
            return findAllByIds(ids, from, size);
        }
    }

    private List<User> findAllByIds(List<Long> ids, int from, int size) {
        int startPage = PageUtil.getStartPage(from, size);

        List<User> list = findAllByPageWithIds(startPage, size, ids);

        if (PageUtil.isTwoSite(from, size)) {
            list.addAll(findAllByPageWithIds(startPage + 1, size, ids));
        }

        return PageUtil.getPageListByPage(list,
                PageUtil.getStartFrom(from, size), size);
    }

    private List<User> findAllWithOutIds(int from, int size) {
        int startPage = PageUtil.getStartPage(from, size);

        List<User> list = findAllByPageWithOutIds(startPage, size);

        if (PageUtil.isTwoSite(from, size)) {
            list.addAll(findAllByPageWithOutIds(startPage + 1, size));
        }

        return PageUtil.getPageListByPage(list,
                PageUtil.getStartFrom(from, size), size);
    }

    private User get(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Не найден пользователь с id: " + userId));
    }

    private List<User> findAllByPageWithIds(int startPage, int size, List<Long> ids) {
        return userRepository.findByIdIn(ids, PageRequest.of(startPage, size));
    }

    private List<User> findAllByPageWithOutIds(int startPage, int size) {
        return userRepository.findAll(PageRequest.of(startPage, size)).stream()
                .collect(Collectors.toList());
    }
}
