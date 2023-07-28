package ru.practicum.servicies.mapperservicies.forall;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.*;
import ru.practicum.enums.EventRequestStatus;
import ru.practicum.mappers.*;
import ru.practicum.servicies.logicservicies.EventRequestService;
import ru.practicum.servicies.logicservicies.EventService;
import ru.practicum.servicies.logicservicies.LikeService;
import ru.practicum.servicies.params.CreateEventParam;
import ru.practicum.servicies.params.CreateEventRequestParam;
import ru.practicum.servicies.params.PatchEventParam;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicUserMapperServiceImpl implements PublicUserMapperService {

    private final EventService eventService;
    private final EventRequestService eventRequestService;
    private final LikeService likeService;


    @Override
    public List<EventFullDto> getEvents(Long userId, Integer from, Integer size) {
        return eventService.findAllByInitiatorId(userId, from, size).stream()
                .map(ExtendEventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventByEventIdAndInitiatorId(Long userId, Long eventId) {
        return ExtendEventMapper.toEventFullDto(
                eventService.getEventByEventIdAndInitiatorId(eventId, userId));
    }

    @Override
    public List<ParticipationRequestDto> getRequestByAnotherEvents(Long userId) {
        return eventRequestService.findAllByRequesterId(userId).stream()
                .map(EventRequestMapper.INSTANCE::toEventParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> getEventParticipants(Long userId, Long eventId) {
        return eventRequestService.findAllByEventIdAndOwnerId(eventId, userId).stream()
                .map(EventRequestMapper.INSTANCE::toEventParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        CreateEventParam createEventParam = CreateEventParam.builder()
                .userId(userId)
                .categoryId(newEventDto.getCategory())
                .location(LocationMapper.INSTANCE.toLocation(newEventDto.getLocation()))
                .build();

        return ExtendEventMapper.toEventFullDto((eventService.create(
                EventMapper.INSTANCE.toEvent(newEventDto), createEventParam)));
    }

    public ParticipationRequestDto addParticipationRequest(Long userId, Long eventId) {
        CreateEventRequestParam createEventRequestParam = CreateEventRequestParam.builder()
                .userId(userId)
                .eventId(eventId)
                .build();

        return EventRequestMapper.INSTANCE.toEventParticipationRequestDto(
                eventRequestService.create(createEventRequestParam));
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId,
                                    UpdateEventUserRequest updateEventUserRequest) {
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

    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        return EventRequestMapper.INSTANCE.toEventParticipationRequestDto(
                eventRequestService.changeStatus(
                        requestId, EventRequestStatus.CANCELED, userId));
    }

    @Override
    public EventRequestStatusUpdateResultDto changeRequestStatus(
            Long userId, Long eventId,
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return EventRequestStatusUpdateResultMapper.eventRequestStatusUpdateResultDto(
                eventRequestService.changeStatusByEvent(
                        eventRequestStatusUpdateRequest, eventId, userId));
    }

    // добавление лайка событию
    @Override
    public LikeDto likeEvent(Long userId, Long eventId) {
        return LikeMapper.INSTANCE.toLikeDto(
                likeService.addReaction(userId, eventId, 1));
    }

    // добавление дизлайка событию
    @Override
    public LikeDto disLikeEvent(Long userId, Long eventId) {
        return LikeMapper.INSTANCE.toLikeDto(
                likeService.addReaction(userId, eventId, -1));
    }

    // удаление лайка
    @Override
    public void deleteLike(Long userId, Long eventId) {
        likeService.deleteReaction(userId, eventId, 1);
    }

    // удаление дизлайка
    @Override
    public void deleteDisLike(Long userId, Long eventId) {
        likeService.deleteReaction(userId, eventId, -1);
    }
}
