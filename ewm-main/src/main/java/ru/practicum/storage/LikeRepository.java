package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Like;
import ru.practicum.model.LikeKey;

import java.util.Optional;

// репозиторий работы с лайками
public interface LikeRepository extends JpaRepository<Like, LikeKey> {

    public Optional<Like> findByUserIdAndEventIdAndStatus(
            Long userId, Long eventId, Integer status);
}
