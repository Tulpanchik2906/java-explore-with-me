package ru.practicum.service.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.ViewStats;
import ru.practicum.mapper.ViewStatsMapper;
import ru.practicum.service.logic.HitService;
import ru.practicum.service.logic.params.StatsSearchParam;
import ru.practicum.util.DateTimeFormatterUtil;

import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsMapperServiceImpl implements StatsMapperService {

    private final HitService hitService;

    @Override
    public List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique) {
        StatsSearchParam statsSearchParam = StatsSearchParam.builder()
                .start(LocalDateTime.parse(URLDecoder.decode(start),
                        DateTimeFormatter.ofPattern(DateTimeFormatterUtil.DATE_TIME_FORMATTER)))
                .end(LocalDateTime.parse(URLDecoder.decode(end),
                        DateTimeFormatter.ofPattern(DateTimeFormatterUtil.DATE_TIME_FORMATTER)))
                .uris(uris)
                .unique(unique)
                .build();

        return hitService.findHits(statsSearchParam).stream()
                .map(ViewStatsMapper.INSTANCE::toViewStats)
                .collect(Collectors.toList());
    }
}
