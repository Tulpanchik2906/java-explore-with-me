package ru.practicum.service.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CreateEndpointHitDto;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.mapper.HitMapper;
import ru.practicum.service.logic.HitService;

@Service
@RequiredArgsConstructor
public class EndpointHitMapperServiceImpl implements EndpointHitMapperService {

    private final HitService hitService;

    @Override
    public EndpointHitDto createHit(CreateEndpointHitDto createEndpointHitDto) {
        return HitMapper.INSTANCE.toEndpointHitDto(
                hitService.add(
                        HitMapper.INSTANCE.toHit(createEndpointHitDto)));
    }
}
