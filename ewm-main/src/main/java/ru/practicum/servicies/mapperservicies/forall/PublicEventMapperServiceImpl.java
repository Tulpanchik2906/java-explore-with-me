package ru.practicum.servicies.mapperservicies.forall;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EventFullDto;
import ru.practicum.exception.ValidationException;
import ru.practicum.mappers.ExtendEventMapper;
import ru.practicum.servicies.logicservicies.EventService;
import ru.practicum.servicies.logicservicies.StatsService;
import ru.practicum.servicies.params.SearchEventParamForUser;
import ru.practicum.util.DateTimeFormatterUtil;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicEventMapperServiceImpl implements PublicEventMapperService {

    private final EventService eventService;
    private final StatsService statsService;

    @Override
    public EventFullDto getEvent(Long id, HttpServletRequest request) {

        EventFullDto eventFullDto =
                ExtendEventMapper.toEventFullDto(eventService.findPublicEvent(id));

        statsService.saveStat(request);
        eventService.saveNewView(id);

        return eventFullDto;
    }

    @Override
    public List<EventFullDto> getEvents(
            String text, List<Long> categories, Boolean paid, String rangeStart,
            String rangeEnd, Boolean onlyAvailable, String sort, Integer from,
            Integer size, HttpServletRequest request) {

        LocalDateTime start = LocalDateTime.now();
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

        SearchEventParamForUser searchEventParamForUser = SearchEventParamForUser.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(start)
                .rangeEnd(end)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .isPublicRequest(true)
                .from(from)
                .size(size)
                .build();

        List<EventFullDto> res = eventService.findAllForUser(searchEventParamForUser).stream()
                .map(ExtendEventMapper::toEventFullDto)
                .collect(Collectors.toList());

        statsService.saveStat(request);

        return res;
    }

}
