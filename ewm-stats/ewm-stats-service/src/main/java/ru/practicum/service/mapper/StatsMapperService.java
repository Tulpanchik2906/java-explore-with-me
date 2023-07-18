package ru.practicum.service.mapper;

import ru.practicum.dto.ViewStats;

import java.util.List;

public interface StatsMapperService {
    public List<ViewStats> getStats(
            String start, String end, List<String> uris, Boolean unique);
}


