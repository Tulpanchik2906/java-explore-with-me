package ru.practicum.servicies.mapperservicies.admin;

import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.UpdateCompilationRequest;

public interface AdminCompilationMapperService {

    CompilationDto saveCompilation(NewCompilationDto newCompilationDto);

    CompilationDto updateCompilation(Long compId,
                                     UpdateCompilationRequest updateCompilationRequest);

    void deleteCompilation(Long compId);
}
