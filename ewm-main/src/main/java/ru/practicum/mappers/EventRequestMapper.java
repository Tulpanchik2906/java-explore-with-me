package ru.practicum.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import ru.practicum.ParticipationRequestDto;
import ru.practicum.model.EventRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventRequestMapper {
    EventRequestMapper INSTANCE = Mappers.getMapper(EventRequestMapper.class);

    @Mapping(target = "event", source = "eventRequest.event.id")
    @Mapping(target = "requester", source = "eventRequest.requester.id")
    ParticipationRequestDto toEventParticipationRequestDto(EventRequest eventRequest);
}
