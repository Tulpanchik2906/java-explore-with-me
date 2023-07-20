package ru.practicum.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.UpdateEventAdminRequest;
import ru.practicum.servicies.mapperservicies.AdminEventMapperService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminEventController {

    private final AdminEventMapperService adminEventMapperService;

    @GetMapping
    public List<EventFullDto> getEventsForAdmin(
            @Valid @RequestParam(value = "users", required = false) List<Long> users,
            @Valid @RequestParam(value = "states", required = false) List<String> states,
            @Valid @RequestParam(value = "categories", required = false) List<Long> categories,
            @Valid @RequestParam(value = "rangeStart", required = false) String rangeStart,
            @Valid @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
            @Valid @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @Valid @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        log.info("Получен запрос на список событий для админа с параметрами:" +
                        "users: {}, states: {}, categories: {}, rangeStart: {}, rangeEnd: {}, " +
                        "from: {}, size: {}.",
                users, states, categories, rangeStart, rangeEnd, from, size);

        return adminEventMapperService.getEventsForAdmin(
                users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Получен запрос на изменение события: c id :{}, body: {}",
                eventId, updateEventAdminRequest.toString());

        return adminEventMapperService.updateEvent(eventId, updateEventAdminRequest);

    }
}
