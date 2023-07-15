package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CreateEndpointHitDto;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.mapper.HitMapper;
import ru.practicum.service.HitService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/hit")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EndpointHitController {

    private final HitService hitService;

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto createHit(
            @Valid @RequestBody CreateEndpointHitDto createEndpointHitDto) {
        log.info("Получен запрос на запись о событии в сервисе статистики.");

        return HitMapper.INSTANCE.toEndpointHitDto(
                hitService.add(
                        HitMapper.INSTANCE.toHit(createEndpointHitDto)));
    }
}
