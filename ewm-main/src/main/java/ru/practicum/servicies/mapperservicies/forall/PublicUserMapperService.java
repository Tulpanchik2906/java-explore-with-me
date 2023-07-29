package ru.practicum.servicies.mapperservicies.forall;

import ru.practicum.dto.*;

import java.util.List;

public interface PublicUserMapperService {

    List<EventFullDto> getEvents(Long userId, Integer from, Integer size);

    EventFullDto getEventByEventIdAndInitiatorId(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequestByAnotherEvents(Long userId);

    List<ParticipationRequestDto> getEventParticipants(Long userId, Long eventId);

    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    ParticipationRequestDto addParticipationRequest(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId,
                             UpdateEventUserRequest updateEventUserRequest);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    EventRequestStatusUpdateResultDto changeRequestStatus(
            Long userId, Long eventId,
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    // добавление лайка событию
    LikeDto likeEvent(Long userId, Long eventId);

    // добавление дизлайка событию
    LikeDto disLikeEvent(Long userId, Long eventId);

    // удаление лайка
    void deleteLike(Long userId, Long eventId);

    // удаление дизлайка
    void deleteDisLike(Long userId, Long eventId);
}
