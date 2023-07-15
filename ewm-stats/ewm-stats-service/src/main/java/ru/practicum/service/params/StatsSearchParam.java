package ru.practicum.service.params;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class StatsSearchParam {
    private LocalDateTime start;
    private LocalDateTime end;
    private List<String> uris;
    private Boolean unique;
}
