package ru.practicum.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.UpdateEventAdminRequest;
import ru.practicum.enums.EventState;
import ru.practicum.exception.ValidationException;
import ru.practicum.mappers.EventMapper;
import ru.practicum.mappers.EventStatusMapper;
import ru.practicum.mappers.ExtendEventMapper;
import ru.practicum.mappers.LocationMapper;
import ru.practicum.model.Event;
import ru.practicum.services.EventService;
import ru.practicum.services.params.PatchEventParam;
import ru.practicum.services.params.SearchEventParamForAdmin;

import javax.validation.Valid;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminEventController {

    private final EventService eventService;
    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;
    private final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    @GetMapping
    public List<EventFullDto> getEvents2(
            @Valid @RequestParam(value = "users", required = false) List<Long> users,
            @Valid @RequestParam(value = "states", required = false) List<String> states,
            @Valid @RequestParam(value = "categories", required = false) List<Long> categories,
            @Valid @RequestParam(value = "rangeStart", required = false) String rangeStart,
            @Valid @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
            @Valid @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @Valid @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart != null) {
            start = LocalDateTime.parse(URLDecoder.decode(rangeStart),
                    DateTimeFormatter.ofPattern(dateTimeFormat));
        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(URLDecoder.decode(rangeEnd),
                    DateTimeFormatter.ofPattern(dateTimeFormat));
        }

        if (rangeEnd != null && rangeStart != null) {
            if (end.isBefore(start)) {
                throw new ValidationException(
                        "Время начала не может быть раньше времени конца. ");
            }
        }

        List<EventState> eventStates = null;
        if (states != null) {
            eventStates = states.stream()
                    .map(x -> EventState.valueOf(x))
                    .collect(Collectors.toList());
        }

        SearchEventParamForAdmin searchEventParamForAdmin = SearchEventParamForAdmin.builder()
                .users(users)
                .states(eventStates)
                .categories(categories)
                .rangeStart(start)
                .rangeEnd(end)
                .from(from)
                .size(size)
                .build();

        return eventService.findAllForAdmin(searchEventParamForAdmin).stream()
                .map(x -> ExtendEventMapper.eventFullDto(
                        eventMapper.toEventFullDto(x), x))
                .collect(Collectors.toList());
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {

        log.info("Получен запрос на изменение события: c id :{}", eventId);

        PatchEventParam patchEventParam = PatchEventParam.builder()
                .eventId(eventId)
                .eventState(EventStatusMapper.toEventStatus(
                        updateEventAdminRequest.getStateAction()))
                .location(locationMapper.toLocation(
                        updateEventAdminRequest.getLocation()))
                .build();

        Event eventRes = eventService.patchByAdmin(eventMapper.toEvent(updateEventAdminRequest),
                patchEventParam);

        return ExtendEventMapper.eventFullDto(
                eventMapper.toEventFullDto(eventRes), eventRes);

    }
}
