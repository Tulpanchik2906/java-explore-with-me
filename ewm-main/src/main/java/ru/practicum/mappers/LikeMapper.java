package ru.practicum.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.LikeDto;
import ru.practicum.model.Like;

/*
    Маппер для конвертации ответа по запросам лайков/дизлайков
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LikeMapper {

    LikeMapper INSTANCE = Mappers.getMapper(LikeMapper.class);

    @Mapping(target = "eventId", source = "like.event.id")
    @Mapping(target = "userId", source = "like.user.id")
    LikeDto toLikeDto(Like like);

}
