package ru.practicum.servicies.mapperservicies.admin;

import ru.practicum.EventFullDto;
import ru.practicum.UpdateEventAdminRequest;

import java.util.List;

public interface AdminEventMapperService {

    List<EventFullDto> getEventsForAdmin(
            List<Long> users,
            List<String> states, List<Long> categories, String rangeStart,
            String rangeEnd, Integer from, Integer size);


    EventFullDto updateEvent(Long eventId,
                             UpdateEventAdminRequest updateEventAdminRequest);
}
