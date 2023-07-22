package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.enums.EventState;
import ru.practicum.util.DateTimeFormatterUtil;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class EventShortDto {
    private String annotation;

    private CategoryDto category;

    private Long confirmedRequests;

    @JsonFormat(pattern = DateTimeFormatterUtil.DATE_TIME_FORMATTER)
    private LocationDto eventDate;

    private Long id;

    private UserShortDto initiator;

    private Boolean paid;

    private String title;

    private Long views;
    private EventState state;
}
