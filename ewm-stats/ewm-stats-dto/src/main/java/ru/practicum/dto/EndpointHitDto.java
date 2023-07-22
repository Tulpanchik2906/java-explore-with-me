package ru.practicum.dto;

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
public class EndpointHitDto {
    private Long id;

    private String app;

    private String uri;

    private String ip;

    @JsonFormat(pattern = DateTimeFormatterUtil.DATE_TIME_FORMATTER)
    private LocalDateTime timestamp;
}
