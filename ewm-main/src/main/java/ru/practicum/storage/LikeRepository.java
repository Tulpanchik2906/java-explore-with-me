package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Like;
import ru.practicum.model.LikeKey;

public interface LikeRepository extends JpaRepository<Like, LikeKey> {
}
