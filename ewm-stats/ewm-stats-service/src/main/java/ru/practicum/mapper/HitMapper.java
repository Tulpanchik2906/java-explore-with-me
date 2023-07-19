package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.CreateEndpointHitDto;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.Hit;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HitMapper {
    HitMapper INSTANCE = Mappers.getMapper(HitMapper.class);

    Hit toHit(CreateEndpointHitDto createEndpointHitDto);

    EndpointHitDto toEndpointHitDto(Hit hit);
}
