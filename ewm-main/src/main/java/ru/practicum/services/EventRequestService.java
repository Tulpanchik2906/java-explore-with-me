package ru.practicum.services;

import ru.practicum.dto.EventRequestStatusUpdateRequest;
import ru.practicum.enums.EventRequestStatus;
import ru.practicum.model.EventRequest;
import ru.practicum.services.params.CreateEventRequestParam;
import ru.practicum.services.params.EventRequestStatusUpdateResult;

import java.util.List;

public interface EventRequestService {

    public List<EventRequest> findAllByEventIdAndOwnerId(
            Long eventId, Long userId);

    public List<EventRequest> findAllByRequesterId(Long userId);

    public EventRequest create(CreateEventRequestParam param);

    public EventRequest changeStatus(
            Long eventRequestId, EventRequestStatus eventRequestStatus, Long userId);

    public EventRequestStatusUpdateResult changeStatusByEvent(
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
            Long eventId, Long userId);

}
