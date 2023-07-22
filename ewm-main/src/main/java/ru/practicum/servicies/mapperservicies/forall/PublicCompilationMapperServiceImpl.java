package ru.practicum.servicies.mapperservicies.forall;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.CompilationDto;
import ru.practicum.mappers.ExtendCompilationMapper;
import ru.practicum.servicies.logicservicies.CompilationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCompilationMapperServiceImpl implements PublicCompilationMapperService {

    private final CompilationService compilationService;

    @Override
    public CompilationDto getCompilation(Long compId) {
        return ExtendCompilationMapper.toCompilationDto(
                compilationService.get(compId));
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        return compilationService.findAll(pinned, from, size).stream()
                .map(x -> ExtendCompilationMapper.toCompilationDto(x))
                .collect(Collectors.toList());
    }
}
