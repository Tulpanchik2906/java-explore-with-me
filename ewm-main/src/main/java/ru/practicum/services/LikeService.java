package ru.practicum.services;

import ru.practicum.model.Like;

public interface LikeService {

    public Like like(Long userId, Long eventId, int status);
}
