package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.ViewStats;
import ru.practicum.service.mapper.StatsMapperService;

import java.util.List;

@RestController
@RequestMapping(path = "/stats")
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatsController {

    private final StatsMapperService statsMapperService;

    @GetMapping
    public List<ViewStats> getStats(
            @RequestParam(required = true) String start,
            @RequestParam(required = true) String end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Получен запрос на получение информации о статистике");

        return statsMapperService.getStats(start, end, uris, unique);
    }
}
