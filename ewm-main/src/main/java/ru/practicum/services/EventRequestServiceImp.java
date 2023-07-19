package ru.practicum.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EventRequestStatusUpdateRequest;
import ru.practicum.enums.EventRequestStatus;
import ru.practicum.enums.EventState;
import ru.practicum.exception.DuplicateException;
import ru.practicum.exception.NotAvailableException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Event;
import ru.practicum.model.EventRequest;
import ru.practicum.model.User;
import ru.practicum.services.params.CreateEventRequestParam;
import ru.practicum.services.params.EventRequestStatusUpdateResult;
import ru.practicum.storage.EventRepository;
import ru.practicum.storage.EventRequestRepository;
import ru.practicum.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventRequestServiceImp implements EventRequestService {
    private final EventRequestRepository eventRequestRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Override
    public List<EventRequest> findAllByEventIdAndOwnerId(
            Long eventId, Long userId) {

        return eventRequestRepository.findByEventIdAndEventInitiatorId(eventId, userId);
    }

    @Override
    public List<EventRequest> findAllByRequesterId(Long userId) {
        return eventRequestRepository.findByRequesterId(userId);
    }

    @Override
    public EventRequest create(CreateEventRequestParam param) {
        User requester = getUser(param.userId);

        Event event = getEvent(param.eventId);

        //Проверка, что событие опубликовано
        if (event.getState() != EventState.PUBLISHED) {
            throw new NotAvailableException("Нельзя подавать заявку на участие в неопубликованном событии.");
        }

        //Проверка, что пользователь еще не подавал заявку на это событие:
        List<EventRequest> requests = eventRequestRepository.findByEventIdAndRequesterId(
                param.eventId, param.userId);

        if (!requests.isEmpty()) {
            throw new DuplicateException(
                    "Пользователь с id: " + param.userId +
                            " уже подавал заявку на событие с id: " +
                            param.eventId);
        }
        //Проверка, что на мероприятии еще есть места
        if (event.getParticipantLimit() != 0 && eventRequestRepository.countByEventIdAndStatus(
                event.getId(), EventRequestStatus.CONFIRMED) >= event.getParticipantLimit()) {
            throw new NotAvailableException("На мероприятии нет свободных мест.");
        }

        //Проверка, что пользователь не иницатор события:
        Optional<Event> eventByOwner =
                eventRepository.findByIdAndInitiatorId(param.eventId, param.getUserId());

        if (eventByOwner.isPresent()) {
            throw new NotAvailableException("Инициатор события не может быть участником события.");
        }

        EventRequest eventRequest = EventRequest.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(requester)
                .build();

        if (event.getRequestModeration() && event.getParticipantLimit() != 0) {
            eventRequest.setStatus(EventRequestStatus.PENDING);
        } else {
            eventRequest.setStatus(EventRequestStatus.CONFIRMED);
        }

        return eventRequestRepository.save(eventRequest);
    }

    @Override
    public EventRequest changeStatus(Long eventRequestId,
                                     EventRequestStatus eventRequestStatus,
                                     Long userId) {
        EventRequest eventRequest = getEventRequest(eventRequestId, userId);
        eventRequest.setStatus(eventRequestStatus);

        return eventRequestRepository.save(eventRequest);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult changeStatusByEvent(
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest, Long eventId,
            Long userId) {
        List<Long> eventRequestIds = eventRequestStatusUpdateRequest.getRequestIds();
        List<EventRequest> confirmRequests = new ArrayList<>();
        List<EventRequest> rejectRequests = new ArrayList<>();
        Optional<Event> event = eventRepository.findById(eventId);

        if (event.isEmpty()) {
            throw new NotFoundException("Событие с id: " + eventId + " не найдено.");
        }

        Long maxConfirmed = event.get().getParticipantLimit();
        Long cntParticipant = eventRequestRepository.countByEventIdAndStatus(
                eventId, EventRequestStatus.CONFIRMED);

        for (int i = 0; i < eventRequestIds.size(); i++) {
            EventRequest eventRequest = getEventRequest(eventRequestIds.get(i), eventId, userId);
            if (eventRequestStatusUpdateRequest.getStatus() == EventRequestStatus.CONFIRMED) {
                if (maxConfirmed == 0 || cntParticipant < maxConfirmed) {
                    // Если заявка еще не подтверждалась
                    if (eventRequest.getStatus() != EventRequestStatus.CONFIRMED) {
                        eventRequest.setStatus(EventRequestStatus.CONFIRMED);
                        confirmRequests.add(eventRequest);
                        cntParticipant++;
                    }
                } else {
                    throw new NotAvailableException("Превышен лимит мест в событии с id: " + eventId);
                }
                eventRequestRepository.save(eventRequest);
            } else {
                if (eventRequest.getStatus() == EventRequestStatus.CONFIRMED) {
                    throw new NotAvailableException("Нельзя отклонить заявку, так как она уже одобрена.");
                }
                eventRequest.setStatus(eventRequestStatusUpdateRequest.getStatus());
                rejectRequests.add(eventRequest);
                eventRequestRepository.save(eventRequest);
            }
        }

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmRequests)
                .rejectedRequests(rejectRequests)
                .build();
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(
                        "Пользователь с id: " + userId + " не найден"));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(
                        "Событие с id: " + eventId + " не найдено"));
    }

    private EventRequest getEventRequest(Long eventRequestId, Long userId) {
        return eventRequestRepository.findByIdAndRequesterId(eventRequestId, userId)
                .orElseThrow(() -> new NotFoundException(
                        "Запрос на участие с id: " + eventRequestId + " не найден" +
                                " для пользователя с id: " + userId));
    }

    private EventRequest getEventRequest(
            Long eventRequestId, Long eventId, Long initiatorId) {
        return eventRequestRepository.findByIdAndEventIdAndEventInitiatorId(
                        eventRequestId, eventId, initiatorId)
                .orElseThrow(() -> new NotFoundException(
                        "Запрос на участие с id: " + eventRequestId + " не найден" +
                                " для пользователя с id: " + initiatorId +
                                " для события c id: " + eventId));
    }

}
