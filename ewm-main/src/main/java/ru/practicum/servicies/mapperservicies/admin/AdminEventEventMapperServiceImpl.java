package ru.practicum.servicies.mapperservicies.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.UpdateEventAdminRequest;
import ru.practicum.enums.EventState;
import ru.practicum.exception.ValidationException;
import ru.practicum.mappers.EventMapper;
import ru.practicum.mappers.EventStatusMapper;
import ru.practicum.mappers.ExtendEventMapper;
import ru.practicum.mappers.LocationMapper;
import ru.practicum.servicies.logicservicies.EventService;
import ru.practicum.servicies.params.PatchEventParam;
import ru.practicum.servicies.params.SearchEventParamForAdmin;
import ru.practicum.util.DateTimeFormatterUtil;

import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminEventEventMapperServiceImpl implements AdminEventMapperService {

    private final EventService eventService;

    @Override
    public List<EventFullDto> getEventsForAdmin(
            List<Long> users,
            List<String> states, List<Long> categories, String rangeStart,
            String rangeEnd, Integer from, Integer size) {

        LocalDateTime start = null;
        LocalDateTime end = null;

        if (rangeStart != null) {
            start = LocalDateTime.parse(URLDecoder.decode(rangeStart),
                    DateTimeFormatter.ofPattern(DateTimeFormatterUtil.DATE_TIME_FORMATTER));
        }

        if (rangeEnd != null) {
            end = LocalDateTime.parse(URLDecoder.decode(rangeEnd),
                    DateTimeFormatter.ofPattern(DateTimeFormatterUtil.DATE_TIME_FORMATTER));
        }

        if (rangeEnd != null && rangeStart != null) {
            if (end.isBefore(start)) {
                throw new ValidationException(
                        "Время начала не может быть раньше времени конца. ");
            }
        }

        List<EventState> eventStates = null;

        if (states != null) {
            eventStates = states.stream()
                    .map(EventState::valueOf)
                    .collect(Collectors.toList());
        }

        SearchEventParamForAdmin searchEventParamForAdmin = SearchEventParamForAdmin.builder()
                .users(users)
                .states(eventStates)
                .categories(categories)
                .rangeStart(start)
                .rangeEnd(end)
                .from(from)
                .size(size)
                .build();

        return eventService.findAllForAdmin(searchEventParamForAdmin).stream()
                .map(ExtendEventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEvent(Long eventId,
                                    UpdateEventAdminRequest updateEventAdminRequest) {

        PatchEventParam patchEventParam = PatchEventParam.builder()
                .eventId(eventId)
                .eventState(EventStatusMapper.toEventStatus(
                        updateEventAdminRequest.getStateAction()))
                .location(LocationMapper.INSTANCE.toLocation(
                        updateEventAdminRequest.getLocation()))
                .build();


        return ExtendEventMapper.toEventFullDto(
                eventService.patchByAdmin(
                        EventMapper.INSTANCE.toEvent(updateEventAdminRequest), patchEventParam));

    }
}
