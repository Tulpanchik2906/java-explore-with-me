package ru.practicum.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.practicum.model.ViewStatsModel;
import ru.practicum.service.logic.params.StatsSearchParam;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HitStorage {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<ViewStatsModel> getViewStats(StatsSearchParam statsSearchParam) {
        StringBuilder stringBuilder = new StringBuilder();

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("start", statsSearchParam.getStart());
        parameters.addValue("end", statsSearchParam.getEnd());

        stringBuilder.append("SELECT app, uri, ");

        if (statsSearchParam.getUnique()) {
            stringBuilder.append("count(distinct ip) as hits ");
        } else {
            stringBuilder.append("count(ip) as hits ");
        }

        stringBuilder.append("FROM hits ");
        stringBuilder.append("WHERE (timestamp BETWEEN :start AND :end) ");

        if (statsSearchParam.getUris() != null) {
            stringBuilder.append("AND (uri IN (:uris)) ");
            parameters.addValue("uris", statsSearchParam.getUris());
        }

        stringBuilder.append("GROUP BY (app, uri) ");
        stringBuilder.append("ORDER BY hits DESC;");

        return jdbcTemplate.query(stringBuilder.toString(),
                parameters,
                new BeanPropertyRowMapper<ViewStatsModel>(ViewStatsModel.class));

    }
}
