package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.ViewStats;
import ru.practicum.service.HitService;
import ru.practicum.service.params.StatsSearchParam;

import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(path = "/stats")
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatsController {

    private final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    private final HitService hitService;

    @GetMapping
    public List<ViewStats> getStats(
            @RequestParam(value = "start", required = true) String start,
            @RequestParam(value = "end", required = true) String end,
            @RequestParam(value = "uris", required = false) List<String> uris,
            @RequestParam(value = "unique", required = false, defaultValue = "false") Boolean unique) {
        log.info("Получен запрос на получение информации о статистике");

        StatsSearchParam statsSearchParam = StatsSearchParam.builder()
                .start(LocalDateTime.parse(URLDecoder.decode(start),
                        DateTimeFormatter.ofPattern(dateTimeFormat)))
                .end(LocalDateTime.parse(URLDecoder.decode(end),
                        DateTimeFormatter.ofPattern(dateTimeFormat)))
                .uris(uris)
                .unique(unique)
                .build();

        return hitService.findHits(statsSearchParam);
    }
}
