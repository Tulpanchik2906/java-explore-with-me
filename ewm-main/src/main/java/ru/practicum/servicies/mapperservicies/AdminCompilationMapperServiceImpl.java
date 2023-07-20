package ru.practicum.servicies.mapperservicies;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.UpdateCompilationRequest;
import ru.practicum.mappers.CompilationMapper;
import ru.practicum.mappers.ExtendCompilationMapper;
import ru.practicum.servicies.logicservicies.CompilationService;

@RequiredArgsConstructor
@Service
public class AdminCompilationMapperServiceImpl implements AdminCompilationMapperService {

    private final CompilationService compilationService;


    @Override
    public CompilationDto saveCompilation(NewCompilationDto newCompilationDto) {
        return ExtendCompilationMapper.toCompilationDto(
                compilationService.create(
                        CompilationMapper.INSTANCE.toCompilation(newCompilationDto),
                        newCompilationDto.getEvents()));
    }

    @Override
    public CompilationDto updateCompilation(Long compId,
                                            UpdateCompilationRequest updateCompilationRequest) {
        return ExtendCompilationMapper.toCompilationDto(
                compilationService.update(compId,
                        CompilationMapper.INSTANCE.toCompilation(updateCompilationRequest),
                        updateCompilationRequest.getEvents()));
    }

    @Override
    public void deleteCompilation(Long compId) {
        compilationService.delete(compId);
    }
}
