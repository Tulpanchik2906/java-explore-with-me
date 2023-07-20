package ru.practicum.controllers.forall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.*;
import ru.practicum.servicies.mapperservicies.forall.PublicUserMapperService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicUserController {

    private final PublicUserMapperService publicUserMapperService;


    @GetMapping("/{userId}/events")
    public List<EventFullDto> getEvents(@PathVariable("userId") Long userId,
                                        @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос на поиск всех событий пользователя с параметрами:" +
                        "userId={}, from={}, size={}.",
                userId, from, size);

        return publicUserMapperService.getEvents(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventByEventIdAndInitiatorId(@PathVariable Long userId,
                                                        @PathVariable Long eventId) {
        log.info("Получен запрос на поиск события {} для иницатора {}.",
                eventId, userId);

        return publicUserMapperService.getEventByEventIdAndInitiatorId(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getRequestByAnotherEvents(@PathVariable Long userId) {
        log.info("Получение информации о заявках пользователя c id: {} на участие в чужих событиях",
                userId);

        return publicUserMapperService.getRequestByAnotherEvents(userId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getEventParticipants(
            @PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получение информации о запросах на участие в событии c id: {}" +
                " пользователя c id:{}", eventId, userId);

        return publicUserMapperService.getEventParticipants(userId, eventId);
    }

    @PostMapping("/{userId}/events")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable("userId") Long userId,
                                    @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Получен запрос на создание события от пользователя {}, body = {}.",
                userId, newEventDto.toString());

        return publicUserMapperService.createEvent(userId, newEventDto);
    }

    @PostMapping("/{userId}/requests")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addParticipationRequest(
            @PathVariable("userId") Long userId,
            @Valid @RequestParam(value = "eventId", required = true) Long eventId) {
        log.info("Получен запрос на участие в событии {} от пользователя {}.",
                eventId, userId);

        return publicUserMapperService.addParticipationRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEvent(
            @PathVariable("userId") Long userId,
            @PathVariable("eventId") Long eventId,
            @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {

        log.info("Получен запрос на изменение события с id:{} от пользователя с id:{}, body={}.",
                eventId, userId, updateEventUserRequest.toString());

        return publicUserMapperService.updateEvent(userId, eventId, updateEventUserRequest);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable("userId") Long userId,
                                                 @PathVariable("requestId") Long requestId) {
        log.info("Получен запрос на отмену участия в событии с параметрами: " +
                "userId={}, requestId={}.", userId, requestId);

        return publicUserMapperService.cancelRequest(userId, requestId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResultDto changeRequestStatus(
            @PathVariable("userId") Long userId,
            @PathVariable("eventId") Long eventId,
            @Valid @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("Получен запрос на изменение статуса запросов на участия на событие с id: {} " +
                        " от пользователя c id: {}, body={}. ", userId, eventId,
                eventRequestStatusUpdateRequest);

        return publicUserMapperService.changeRequestStatus(userId, eventId,
                eventRequestStatusUpdateRequest);
    }

    @PutMapping("/{userId}/like/{eventId}")
    @ResponseBody
    public LikeDto likeEvent(
            @PathVariable("userId") Long userId,
            @PathVariable("eventId") Long eventId) {
        log.info("Получен запрос на лайк событию {} от пользователя {}.",
                eventId, userId);

        return publicUserMapperService.likeEvent(userId, eventId);
    }

    @PutMapping("/{userId}/dislike/{eventId}")
    @ResponseBody
    public LikeDto disLikeEvent(
            @PathVariable("userId") Long userId,
            @PathVariable(value = "eventId") Long eventId) {
        log.info("Получен запрос на дизлайк событию {} от пользователя {}.",
                eventId, userId);

        return publicUserMapperService.disLikeEvent(userId, eventId);
    }
}
