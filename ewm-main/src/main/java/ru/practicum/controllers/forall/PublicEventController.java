package ru.practicum.controllers.forall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.servicies.mapperservicies.forall.PublicEventMapperService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicEventController {

    private final PublicEventMapperService publicEventMapperService;

    @GetMapping("/{id}")
    public EventFullDto getEvent(@PathVariable("id") Long id, HttpServletRequest request) {
        log.info("Получен запрос на получение данных о событии с id: {}.", id);

        return publicEventMapperService.getEvent(id, request);
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
        log.info("Получен публичный запрос на список событий со следующими параметрами:" +
                        " text={}, categories={}, paid={}, rangeStart={}," +
                        " rangeEnd={}, onlyAvailable={}, sort={}, from={}, size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        return publicEventMapperService.getEvents(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size, request);
    }

}
