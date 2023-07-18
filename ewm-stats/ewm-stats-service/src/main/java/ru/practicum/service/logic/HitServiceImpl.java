package ru.practicum.service.logic;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.model.Hit;
import ru.practicum.model.ViewStatsModel;
import ru.practicum.service.logic.params.StatsSearchParam;
import ru.practicum.storage.HitRepository;
import ru.practicum.storage.HitStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class HitServiceImpl implements HitService {

    private final HitRepository hitRepository;

    private final HitStorage hitStorage;

    @Override
    public Hit add(Hit hit) {
        return hitRepository.save(hit);
    }

    @Override
    public List<ViewStatsModel> findHits(StatsSearchParam statsSearchParam) {
        return hitStorage.getViewStats(statsSearchParam);
    }
}
