package ru.practicum.controllers.forall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicCompilationController {

    private final PublicCompilationController publicCompilationController;

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable("compId") Long compId) {
        log.info("Получен запрос на получение подборки с compId: {}.", compId);

        return publicCompilationController.getCompilation(compId);
    }

    @GetMapping
    public List<CompilationDto> getCompilations(
            @RequestParam(required = false) Boolean pinned,
            @PositiveOrZero @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        log.info("Получен запрос на получение списка подборок с параметрами: " +
                "pinned={}, from={}, size={}", pinned, from, size);

        return publicCompilationController.getCompilations(pinned, from, size);
    }

}
