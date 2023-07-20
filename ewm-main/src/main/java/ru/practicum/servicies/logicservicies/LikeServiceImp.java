package ru.practicum.servicies.logicservicies;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Event;
import ru.practicum.model.Like;
import ru.practicum.model.User;
import ru.practicum.storage.EventRepository;
import ru.practicum.storage.LikeRepository;
import ru.practicum.storage.UserRepository;

@Service
@RequiredArgsConstructor
public class LikeServiceImp implements LikeService {

    private final LikeRepository likeRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Override
    public Like like(Long userId, Long eventId, int status) {
        Like like = Like.builder()
                .userId(userId)
                .eventId(eventId)
                .user(getUser(userId))
                .event(getEvent(eventId))
                .status(status)
                .build();

        return likeRepository.save(like);
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


}
