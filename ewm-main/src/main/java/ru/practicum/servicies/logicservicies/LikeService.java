package ru.practicum.servicies.logicservicies;

import ru.practicum.model.Like;

public interface LikeService {

    public Like like(Long userId, Long eventId, int status);
}
