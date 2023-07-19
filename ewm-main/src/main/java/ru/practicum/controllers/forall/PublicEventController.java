package ru.practicum.controllers.forall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.exception.ValidationException;
import ru.practicum.mappers.EventMapper;
import ru.practicum.mappers.ExtendEventMapper;
import ru.practicum.model.Event;
import ru.practicum.services.EventService;
import ru.practicum.services.StatsService;
import ru.practicum.services.params.SearchEventParamForUser;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicEventController {

    private final EventService eventService;
    private final EventMapper eventMapper;
    private final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    private final StatsService statsService;

    @GetMapping("/{id}")
    public EventFullDto getEvent(@PathVariable("id") Long id, HttpServletRequest request) {

        Event eventRes = eventService.findPublicEvent(id);

        EventFullDto eventFullDto = ExtendEventMapper.eventFullDto(
                eventMapper.toEventFullDto(eventRes), eventRes);

        statsService.saveStat(request);
        eventService.saveNewView(id);

        return eventFullDto;
    }

    @GetMapping
    public List<EventFullDto> getEvents(
            @Size(min = 1, max = 7000) @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "categories", required = false) List<Long> categories,
            @RequestParam(value = "paid", required = false) Boolean paid,
            @RequestParam(value = "rangeStart", required = false) String rangeStart,
            @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(value = "onlyAvailable", required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(value = "sort", required = false) String sort,
            @PositiveOrZero @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            HttpServletRequest request) {

        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart != null) {
            start = LocalDateTime.parse(URLDecoder.decode(rangeStart),
                    DateTimeFormatter.ofPattern(dateTimeFormat));
        } else {
            start = LocalDateTime.now();
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

        SearchEventParamForUser searchEventParamForUser = SearchEventParamForUser.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(start)
                .rangeEnd(end)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .isPublicRequest(true)
                .from(from)
                .size(size)
                .build();

        List<EventFullDto> res = eventService.findAllForUser(searchEventParamForUser).stream()
                .map(x -> ExtendEventMapper.eventFullDto(
                        eventMapper.toEventFullDto(x), x))
                .collect(Collectors.toList());

        statsService.saveStat(request);

        return res;
    }

}
