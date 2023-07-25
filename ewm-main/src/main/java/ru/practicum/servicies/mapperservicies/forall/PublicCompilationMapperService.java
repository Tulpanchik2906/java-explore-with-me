package ru.practicum.servicies.mapperservicies.forall;

import ru.practicum.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationMapperService {

    CompilationDto getCompilation(Long compId);

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);
}
