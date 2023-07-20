package ru.practicum.controllers.forall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.*;
import ru.practicum.enums.EventRequestStatus;
import ru.practicum.mappers.*;
import ru.practicum.servicies.logicservicies.EventRequestService;
import ru.practicum.servicies.logicservicies.EventService;
import ru.practicum.servicies.logicservicies.LikeService;
import ru.practicum.servicies.params.CreateEventParam;
import ru.practicum.servicies.params.CreateEventRequestParam;
import ru.practicum.servicies.params.PatchEventParam;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicUserController {

    private final EventService eventService;
    private final EventRequestService eventRequestService;
    private final LikeService likeService;


    @GetMapping("/{userId}/events")
    public List<EventFullDto> getEvents(@PathVariable("userId") Long userId,
                                        @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос на поиск всех событий пользователя {}.",
                userId);


        return eventService.findAllByInitiatorId(userId, from, size).stream()
                .map(ExtendEventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventByEventIdAndInitiatorId(@PathVariable Long userId,
                                                        @PathVariable Long eventId) {
        log.info("Получен запрос на поиск события {} для иницатора {}.",
                eventId, userId);

        return ExtendEventMapper.toEventFullDto(
                eventService.getEventByEventIdAndInitiatorId(eventId, userId));
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getRequestByAnotherEvents(@PathVariable Long userId) {
        return eventRequestService.findAllByRequesterId(userId).stream()
                .map(x -> EventRequestMapper.INSTANCE.toEventParticipationRequestDto(x))
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getEventParticipants(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        return eventRequestService.findAllByEventIdAndOwnerId(eventId, userId).stream()
                .map(x -> EventRequestMapper.INSTANCE.toEventParticipationRequestDto(x))
                .collect(Collectors.toList());
    }

    @PostMapping("/{userId}/events")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable("userId") Long userId,
                                    @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Получен запрос на создание события от пользователя {}.",
                userId);

        CreateEventParam createEventParam = CreateEventParam.builder()
                .userId(userId)
                .categoryId(newEventDto.getCategory())
                .location(LocationMapper.INSTANCE.toLocation(newEventDto.getLocation()))
                .build();

        return ExtendEventMapper.toEventFullDto((eventService.create(
                EventMapper.INSTANCE.toEvent(newEventDto), createEventParam)));
    }

    @PostMapping("/{userId}/requests")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addParticipationRequest(
            @PathVariable("userId") Long userId,
            @Valid @RequestParam(value = "eventId", required = true) Long eventId) {
        log.info("Получен запрос на участие в событии {} от пользователя {}.",
                eventId, userId);
        CreateEventRequestParam createEventRequestParam = CreateEventRequestParam.builder()
                .userId(userId)
                .eventId(eventId)
                .build();
        return EventRequestMapper.INSTANCE.toEventParticipationRequestDto(
                eventRequestService.create(createEventRequestParam));
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable("userId") Long userId,
                                    @PathVariable("eventId") Long eventId,
                                    @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {

        log.info("Получен запрос на изменение события с id:{} от пользователя с id:{} ",
                eventId, userId);

        log.info(updateEventUserRequest.toString());

        PatchEventParam patchEventParam = PatchEventParam.builder()
                .eventId(eventId)
                .userId(userId)
                .eventState(EventStatusMapper.toEventStatus(
                        updateEventUserRequest.getStateAction()))
                .location(LocationMapper.INSTANCE.toLocation(
                        updateEventUserRequest.getLocation()))
                .build();

        return ExtendEventMapper.toEventFullDto(
                eventService.patchByUser(EventMapper.INSTANCE.toEvent(updateEventUserRequest),
                        patchEventParam));
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable("userId") Long userId,
                                                 @PathVariable("requestId") Long requestId) {
        return EventRequestMapper.INSTANCE.toEventParticipationRequestDto(
                eventRequestService.changeStatus(
                        requestId, EventRequestStatus.CANCELED, userId));
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResultDto changeRequestStatus(
            @PathVariable("userId") Long userId,
            @PathVariable("eventId") Long eventId,
            @Valid @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("Получен запрос на изменение статуса запросов на участия на событие с id: " +
                eventId + " от пользователя c id: " + userId);

        log.info(eventRequestStatusUpdateRequest.toString());
        return EventRequestStatusUpdateResultMapper.eventRequestStatusUpdateResultDto(
                eventRequestService.changeStatusByEvent(
                        eventRequestStatusUpdateRequest, eventId, userId));
    }

    @PutMapping("/{userId}/like/{eventId}")
    @ResponseBody
    public LikeDto likeEvent(
            @PathVariable("userId") Long userId,
            @PathVariable("eventId") Long eventId) {
        log.info("Получен запрос на лайк событию {} от пользователя {}.",
                eventId, userId);

        return LikeMapper.INSTANCE.toLikeDto(
                likeService.like(userId, eventId, 1));
    }

    @PutMapping("/{userId}/dislike/{eventId}")
    @ResponseBody
    public LikeDto disLikeEvent(
            @PathVariable("userId") Long userId,
            @PathVariable(value = "eventId") Long eventId) {
        log.info("Получен запрос на лайк событию {} от пользователя {}.",
                eventId, userId);

        return LikeMapper.INSTANCE.toLikeDto(
                likeService.like(userId, eventId, -1));
    }
}
