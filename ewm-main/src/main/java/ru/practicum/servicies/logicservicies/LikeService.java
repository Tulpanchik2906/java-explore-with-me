package ru.practicum.servicies.logicservicies;

import ru.practicum.model.Like;

public interface LikeService {

    public Like addReaction(Long userId, Long eventId, int status);

    public void deleteReaction(Long userId, Long eventId, int status);

}
