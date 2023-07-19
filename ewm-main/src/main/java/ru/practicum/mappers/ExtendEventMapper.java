package ru.practicum.mappers;

import ru.practicum.dto.EventFullDto;
import ru.practicum.model.Event;

public class ExtendEventMapper {

    public static EventFullDto eventFullDto(
            EventFullDto eventFullDto, Event event) {
        eventFullDto.setCategory(
                CategoryMapper.INSTANCE.toCategoryDto(event.getCategory()));

        eventFullDto.setLocation(
                LocationMapper.INSTANCE.toLocationDto(event.getLocation()));

        eventFullDto.setInitiator(
                UserMapper.INSTANCE.toUserShortDto(event.getInitiator()));

        return eventFullDto;
    }

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
