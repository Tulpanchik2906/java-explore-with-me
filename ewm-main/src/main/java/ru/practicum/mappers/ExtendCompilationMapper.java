package ru.practicum.mappers;

import ru.practicum.dto.CompilationDto;
import ru.practicum.model.Compilation;

import java.util.stream.Collectors;

public class ExtendCompilationMapper {

    public static CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = CompilationMapper.INSTANCE
                .toCompilationDto(compilation);

        if (compilation.getEvents() != null) {
            compilationDto.setEvents(
                    compilation.getEvents().stream()
                            .map(x -> ExtendEventMapper.toEventFullDto(x))
                            .collect(Collectors.toList())
            );
        }

        return compilationDto;
    }
}
