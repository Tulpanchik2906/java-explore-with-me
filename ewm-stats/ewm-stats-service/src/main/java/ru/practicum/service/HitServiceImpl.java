package ru.practicum.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.ViewStats;
import ru.practicum.model.Hit;
import ru.practicum.model.QHit;
import ru.practicum.service.params.StatsSearchParam;
import ru.practicum.storage.HitRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HitServiceImpl implements HitService {

    private final HitRepository hitRepository;

    @Override
    public Hit add(Hit hit) {
        return hitRepository.save(hit);
    }

    @Override
    public List<ViewStats> findHits(StatsSearchParam statsSearchParam) {
        BooleanExpression byStart = QHit.hit.timestamp.after(statsSearchParam.getStart());
        BooleanExpression byEnd = QHit.hit.timestamp.before(statsSearchParam.getEnd());

        BooleanExpression query = byStart.and(byEnd);

        if (statsSearchParam.getUris() != null) {
            BooleanExpression byUris = QHit.hit.uri.in(statsSearchParam.getUris());
            query = query.and(byUris);
        }

        Iterable<Hit> foundItems = hitRepository.findAll(query);
        Map<String, Hit> map = new HashMap<>();
        for (Hit hit : foundItems) {
            map.put(hit.getUri(), hit);
        }

        List<Hit> list = map.values().stream().collect(Collectors.toList());

        if (statsSearchParam.getUnique() == false) {
            return list.stream()
                    .map(x -> ViewStats.builder()
                            .uri(x.getUri())
                            .app(x.getApp())
                            .hits(hitRepository.countByUri(x.getUri()))
                            .build())
                    .collect(Collectors.toList()).stream().sorted(
                            (o1, o2) -> Long.compare(o2.getHits(), o1.getHits()))
                    .collect(Collectors.toList());
        } else {
            return list.stream()
                    .map(x -> ViewStats.builder()
                            .uri(x.getUri())
                            .app(x.getApp())
                            .hits(hitRepository.countDistinctIpByUri(x.getUri()))
                            .build())
                    .collect(Collectors.toList()).stream().sorted(
                            (o1, o2) -> Long.compare(o2.getHits(), o1.getHits()))
                    .collect(Collectors.toList());
        }
    }
}
