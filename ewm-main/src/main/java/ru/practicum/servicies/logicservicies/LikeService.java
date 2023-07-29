package ru.practicum.servicies.logicservicies;

import ru.practicum.model.Like;

/*
    Сервис работы с лайками и расчетом рейтинига
 */
public interface LikeService {

    // добавление реакции
    public Like addReaction(Long userId, Long eventId, int status);

    // удаление реакции
    public void deleteReaction(Long userId, Long eventId, int status);

}
