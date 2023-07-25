package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.annotation.NotBlankWithNull;
import ru.practicum.enums.StateAction;
import ru.practicum.util.DateTimeFormatterUtil;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UpdateEventAdminRequest {

    @Length(min = 20, max = 2000)
    @NotBlankWithNull
    private String annotation;

    private Long category;

    @Length(min = 20, max = 7000)
    @NotBlankWithNull
    private String description;

    @JsonFormat(pattern = DateTimeFormatterUtil.DATE_TIME_FORMATTER)
    @Future
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private StateAction stateAction;

    @Length(min = 3, max = 120)
    @NotBlankWithNull
    private String title;
}
