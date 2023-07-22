package ru.practicum.servicies.mapperservicies.admin;

import ru.practicum.CompilationDto;
import ru.practicum.NewCompilationDto;
import ru.practicum.UpdateCompilationRequest;

public interface AdminCompilationMapperService {

    CompilationDto saveCompilation(NewCompilationDto newCompilationDto);

    CompilationDto updateCompilation(Long compId,
                                     UpdateCompilationRequest updateCompilationRequest);

    void deleteCompilation(Long compId);
}
