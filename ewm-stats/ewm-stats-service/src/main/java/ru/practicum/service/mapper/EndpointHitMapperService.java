package ru.practicum.service.mapper;

import ru.practicum.dto.CreateEndpointHitDto;
import ru.practicum.dto.EndpointHitDto;

public interface EndpointHitMapperService {
    EndpointHitDto createHit(CreateEndpointHitDto createEndpointHitDto);
}
