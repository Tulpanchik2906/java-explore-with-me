package ru.practicum.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    public List<User> findByIdIn(List<Long> ids);

    public List<User> findByIdIn(List<Long> ids, Pageable pageable);

}
