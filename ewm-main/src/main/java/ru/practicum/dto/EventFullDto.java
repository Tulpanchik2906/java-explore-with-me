package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.enums.EventState;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class EventFullDto {
    private String annotation;

    private CategoryDto category;

    private Long confirmedRequests;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Long id;

    private UserShortDto initiator;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit = 0;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String publishedOn;

    private Boolean requestModeration = true;

    private String title;

    private EventState state;

    private Long views;

    private double rating;
}
