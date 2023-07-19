package ru.practicum.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.UpdateCompilationRequest;
import ru.practicum.mappers.CompilationMapper;
import ru.practicum.mappers.ExtendCompilationMapper;
import ru.practicum.services.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminCompilationController {

    private final CompilationService compilationService;

    private final CompilationMapper compilationMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto saveCompilation(
            @Valid @RequestBody NewCompilationDto newCompilationDto) {
        return ExtendCompilationMapper.toCompilationDto(
                compilationService.create(
                        compilationMapper.toCompilation(newCompilationDto),
                        newCompilationDto.getEvents()));
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable("compId") Long compId,
                                            @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        return ExtendCompilationMapper.toCompilationDto(
                compilationService.update(compId,
                        compilationMapper.toCompilation(updateCompilationRequest),
                        updateCompilationRequest.getEvents()));
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable("compId") Long compId) {
        compilationService.delete(compId);
    }
}
