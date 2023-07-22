package ru.practicum.controllers.forall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.CategoryDto;
import ru.practicum.servicies.mapperservicies.forall.PublicCategoryMapperService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicCategoryController {
    private final PublicCategoryMapperService publicCategoryMapperService;

    @GetMapping
    @ResponseBody
    public List<CategoryDto> getCategories(
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос на получение списка категорий с параметрами: " +
                "from={}, size={}.", from, size);

        return publicCategoryMapperService.getCategories(from, size);
    }

    @GetMapping("/{id}")
    public CategoryDto getCategory(@PathVariable Long id) {
        log.info("Получен запрос на получение категории с id: {}.", id);

        return publicCategoryMapperService.getCategory(id);
    }
}
