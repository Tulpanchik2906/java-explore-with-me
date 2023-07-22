package ru.practicum.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.dto.UpdateEventAdminRequest;
import ru.practicum.dto.UpdateEventUserRequest;
import ru.practicum.model.Event;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "category", ignore = true)
    Event toEvent(NewEventDto newEventDto);

    @Mapping(target = "category", ignore = true)
    Event toEvent(UpdateEventAdminRequest updateEventAdminRequest);

    @Mapping(target = "category", ignore = true)
    Event toEvent(UpdateEventUserRequest updateEventUserRequest);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "location", ignore = true)
    EventFullDto toEventFullDto(Event event);

}
