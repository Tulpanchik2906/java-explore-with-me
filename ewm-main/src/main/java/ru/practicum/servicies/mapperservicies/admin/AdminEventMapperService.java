package ru.practicum.servicies.mapperservicies.admin;

import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.UpdateEventAdminRequest;

import java.util.List;

public interface AdminEventMapperService {

    List<EventFullDto> getEventsForAdmin(
            List<Long> users,
            List<String> states, List<Long> categories, String rangeStart,
            String rangeEnd, Integer from, Integer size);


    EventFullDto updateEvent(Long eventId,
                             UpdateEventAdminRequest updateEventAdminRequest);
}
