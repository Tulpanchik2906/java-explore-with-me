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

    private final LikeRepository likeRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Override
    @Transactional
    public Like addReaction(Long userId, Long eventId, int status) {
        User user = getUser(userId);
        Event event = getEvent(eventId);

        if (event.getState() != EventState.PUBLISHED) {
            throw new NotAvailableException(
                    "Лайки можно ставить только опубликованным событиям");
        }

        Like like = Like.builder()
                .userId(userId)
                .eventId(eventId)
                .user(user)
                .event(event)
                .status(status)
                .build();

        Optional<Like> oldLike = likeRepository.findByUserIdAndEventIdAndStatus(
                userId, eventId, status);

        if (oldLike.isPresent() &&
                oldLike.get().getStatus() == status) {
            throw new DuplicateException(
                    "Такой статус уже выставлен пользователем " + userId +
                            "для события: " + eventId);
        }

        if (oldLike.isPresent()) {
            deleteReaction(userId, eventId, oldLike.get().getStatus());
        }

        changeRatingAfterAddReaction(eventId, status);

        return likeRepository.save(like);
    }

    @Override
    @Transactional
    public void deleteReaction(Long userId, Long eventId, int status) {
        LikeKey likeKey = new LikeKey(userId, eventId);
        Like like = getLikeByLikeKey(likeKey);

        if (status != like.getStatus()) {
            throw new NotAvailableException(
                    "Попытка удалить противоположную реакцию");
        }

        if (like.getStatus() == 1) {
            changeRatingAfterDelete(eventId, 1);
        }

        if (like.getStatus() == -1) {
            changeRatingAfterDelete(eventId, -1);
        }

        likeRepository.deleteById(likeKey);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(
                        "Пользователь с id: " + userId + " не найден."));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(
                        "Событие с id: " + eventId + " не найдена."));
    }

    private Like getLikeByLikeKey(LikeKey likeKey) {
        return likeRepository.findById(likeKey).orElseThrow(
                () -> new NotFoundException("Лайк не найден"));
    }

    private void changeRatingAfterDelete(Long eventId, int change) {
        Event event = getEvent(eventId);
        event.setRating(event.getRating() - change);
        eventRepository.save(event);
        event.getInitiator().setRating(
                event.getInitiator().getRating() - change);
        userRepository.save(event.getInitiator());
    }

    private void changeRatingAfterAddReaction(Long eventId, int change) {
        Event event = getEvent(eventId);
        event.setRating(event.getRating() + change);
        eventRepository.save(event);
        event.getInitiator().setRating(
                event.getInitiator().getRating() + change);
        userRepository.save(event.getInitiator());
    }
}
