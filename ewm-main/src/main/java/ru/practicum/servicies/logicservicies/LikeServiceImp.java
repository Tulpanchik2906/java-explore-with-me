package ru.practicum.servicies.logicservicies;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.enums.EventState;
import ru.practicum.exception.DuplicateException;
import ru.practicum.exception.NotAvailableException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Event;
import ru.practicum.model.Like;
import ru.practicum.model.LikeKey;
import ru.practicum.model.User;
import ru.practicum.storage.EventRepository;
import ru.practicum.storage.LikeRepository;
import ru.practicum.storage.UserRepository;

import java.util.Optional;

/*
    Сервис работы с лайками и расчетом рейтинига
 */
@Service
@RequiredArgsConstructor
public class LikeServiceImp implements LikeService {

    private final UserRepository userRepository;

    private final LikeRepository likeRepository;

    private final EventRepository eventRepository;

    /*
        Метод добавления лайка/дизлайка
     */
    @Override
    @Transactional
    public Like addReaction(Long userId, Long eventId, int status) {
        Event event = getEvent(eventId);
        User user = getUser(userId);

        // проверка на опубликованность события
        if (event.getState() != EventState.PUBLISHED) {
            throw new NotAvailableException("Лайки можно ставить только опубликованным событиям");
        }

        Like like = Like.builder()
                .eventId(eventId)
                .userId(userId)
                .user(user)
                .event(event)
                .status(status)
                .build();

        // Проверяем есть ли уже по этим id лайки или дизлайки
        Optional<Like> oldLike = likeRepository.findByUserIdAndEventId(
                userId, eventId);

        // Проверка, что не пытаемся поставить повторный лайк
        if (oldLike.isPresent() && oldLike.get().getStatus() == status) {
            throw new DuplicateException(
                    "Такой статус уже выставлен пользователем "
                            + userId + "для события: " + eventId);
        }

        // Если реакции есть, то удаляем старую реакцию и добавляем новую.
        if (oldLike.isPresent()) {
            deleteReaction(userId, eventId, oldLike.get().getStatus());
        }

        // Пересчет рейтинга
        changeRatingAfterAddReaction(eventId, status);

        // Сохранить обновления
        return likeRepository.save(like);
    }

    /*
         Метод удаления лайка/дизлайка
    */
    @Override
    @Transactional
    public void deleteReaction(Long userId, Long eventId, int status) {
        LikeKey likeKey = new LikeKey(userId, eventId);
        Like like = getLikeByLikeKey(likeKey);

        if (status != like.getStatus()) {
            throw new NotAvailableException(
                    "Попытка удалить противоположную реакцию");
        }

        // меняем рейтинг события и пользователя в зависимости от статуса
        if (like.getStatus() == 1) {
            changeRatingAfterDelete(eventId, 1);
        }

        if (like.getStatus() == -1) {
            changeRatingAfterDelete(eventId, -1);
        }

        likeRepository.deleteById(likeKey);
    }

    // поиск пользователя
    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        "Пользователь с id: " + userId + " не найден."));
    }

    // поиск события
    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(
                        "Событие с id: " + eventId + " не найдена."));
    }

    // поиск лайка
    private Like getLikeByLikeKey(LikeKey likeKey) {
        return likeRepository.findById(likeKey)
                .orElseThrow(() -> new NotFoundException("Лайк не найден"));
    }

    // изменение рейтинга после удаления реакции
    private void changeRatingAfterDelete(Long eventId, int change) {
        Event event = getEvent(eventId);
        event.setRating(event.getRating() - change);

        eventRepository.save(event);

        event.getInitiator().setRating(
                event.getInitiator().getRating() - change);

        userRepository.save(event.getInitiator());
    }

    // изменение рейтинга после добавления реакции
    private void changeRatingAfterAddReaction(Long eventId, int change) {
        Event event = getEvent(eventId);
        event.setRating(event.getRating() + change);

        eventRepository.save(event);

        event.getInitiator().setRating(
                event.getInitiator().getRating() + change);

        userRepository.save(event.getInitiator());
    }
}
