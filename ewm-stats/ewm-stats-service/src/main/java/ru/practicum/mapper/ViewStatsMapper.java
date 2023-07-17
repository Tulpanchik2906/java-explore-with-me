package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.ViewStats;
import ru.practicum.model.ViewStatsModel;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ViewStatsMapper {
    ViewStatsMapper INSTANCE = Mappers.getMapper(ViewStatsMapper.class);

    ViewStats toViewStats(ViewStatsModel viewStatsModel);
}
