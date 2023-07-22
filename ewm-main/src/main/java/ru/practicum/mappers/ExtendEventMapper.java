package ru.practicum.mappers;

import ru.practicum.EventFullDto;
import ru.practicum.model.Event;

public class ExtendEventMapper {

    public static EventFullDto toEventFullDto(Event event) {
        EventFullDto eventFullDto = EventMapper.INSTANCE.toEventFullDto(event);

        eventFullDto.setCategory(
                CategoryMapper.INSTANCE.toCategoryDto(event.getCategory()));

        eventFullDto.setLocation(
                LocationMapper.INSTANCE.toLocationDto(event.getLocation()));

        eventFullDto.setInitiator(
                UserMapper.INSTANCE.toUserShortDto(event.getInitiator()));

        return eventFullDto;
    }
}
