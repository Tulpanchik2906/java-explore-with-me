package ru.practicum.service;

import ru.practicum.model.Hit;
import ru.practicum.model.ViewStatsModel;
import ru.practicum.service.params.StatsSearchParam;

import java.util.List;

public interface HitService {
    public Hit add(Hit hit);

    public List<ViewStatsModel> findHits(StatsSearchParam statsSearchParam);
}
