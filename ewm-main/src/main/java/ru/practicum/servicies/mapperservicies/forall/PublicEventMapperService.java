package ru.practicum.servicies.mapperservicies.forall;

import ru.practicum.dto.EventFullDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PublicEventMapperService {

    EventFullDto getEvent(Long id, HttpServletRequest request);

    List<EventFullDto> getEvents(
            String text, List<Long> categories, Boolean paid, String rangeStart,
            String rangeEnd, Boolean onlyAvailable, String sort, Integer from,
            Integer size, HttpServletRequest request);
}
