package ru.practicum.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EventFullDto;
import ru.practicum.UpdateEventAdminRequest;
import ru.practicum.servicies.mapperservicies.admin.AdminEventMapperService;

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
            @Valid @RequestParam(required = false) List<Long> users,
            @Valid @RequestParam(required = false) List<String> states,
            @Valid @RequestParam(required = false) List<Long> categories,
            @Valid @RequestParam(required = false) String rangeStart,
            @Valid @RequestParam(required = false) String rangeEnd,
            @Valid @RequestParam(defaultValue = "0") Integer from,
            @Valid @RequestParam(defaultValue = "10") Integer size) {
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
