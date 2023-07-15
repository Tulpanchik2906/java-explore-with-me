package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ViewStats {

    private String app;

    private String uri;

    private Long hits;
}
