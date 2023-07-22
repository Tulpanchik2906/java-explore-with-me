package ru.practicum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.util.DateTimeFormatterUtil;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ParticipationRequestDto {
    @JsonFormat(pattern = DateTimeFormatterUtil.DATE_TIME_FORMATTER)
    private LocalDateTime created;

    private Long event;

    private Long id;

    private Long requester;

    private String status;
}
