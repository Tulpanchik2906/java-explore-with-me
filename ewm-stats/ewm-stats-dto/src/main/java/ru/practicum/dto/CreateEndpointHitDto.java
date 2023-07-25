package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.util.DateTimeFormatterUtil;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateEndpointHitDto {

    @NotNull
    @NotBlank
    private String app;

    @NotNull
    @NotBlank
    private String uri;

    @NotNull
    @NotBlank
    private String ip;

    @NotNull
    @JsonFormat(pattern = DateTimeFormatterUtil.DATE_TIME_FORMATTER)
    private LocalDateTime timestamp;
}
