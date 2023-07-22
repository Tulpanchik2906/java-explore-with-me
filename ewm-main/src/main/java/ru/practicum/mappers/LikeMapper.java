package ru.practicum.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import ru.practicum.LikeDto;
import ru.practicum.model.Like;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LikeMapper {
    LikeMapper INSTANCE = Mappers.getMapper(LikeMapper.class);

    @Mapping(target = "userId", source = "like.user.id")
    @Mapping(target = "eventId", source = "like.event.id")
    LikeDto toLikeDto(Like like);

}
