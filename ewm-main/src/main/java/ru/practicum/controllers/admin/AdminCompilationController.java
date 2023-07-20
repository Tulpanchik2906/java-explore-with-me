package ru.practicum.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.UpdateCompilationRequest;
import ru.practicum.servicies.mapperservicies.admin.AdminCompilationMapperService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminCompilationController {

    private final AdminCompilationMapperService adminCompilationMapperService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto saveCompilation(
            @Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Получен запрос на сохранение новой подборки, body={}",
                newCompilationDto.toString());
        return adminCompilationMapperService.saveCompilation(newCompilationDto);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable("compId") Long compId,
                                            @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.info("Получен запрос на изменение подборки, compId = {}, body={}", compId,
                updateCompilationRequest.toString());
        return adminCompilationMapperService.updateCompilation(compId, updateCompilationRequest);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable("compId") Long compId) {
        log.info("Получен запрос на удаление подборки с id: {}", compId);
        adminCompilationMapperService.deleteCompilation(compId);
    }
}
