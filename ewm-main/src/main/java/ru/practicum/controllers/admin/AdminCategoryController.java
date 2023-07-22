package ru.practicum.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.CategoryDto;
import ru.practicum.NewCategoryDto;
import ru.practicum.servicies.mapperservicies.admin.AdminCategoryMapperService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminCategoryController {

    private final AdminCategoryMapperService adminCategoryMapperService;

    @GetMapping
    @ResponseBody
    public List<CategoryDto> getCategories(
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос на просмотр списка категорий. " +
                "Параметры запроса: from: {}, size: {}. ", from, size);

        return adminCategoryMapperService.getCategories(from, size);
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Получен запрос на добавление новой категории: {} ",
                newCategoryDto.toString());
        return adminCategoryMapperService.addCategory(newCategoryDto);
    }

    @PatchMapping("/{id}")
    @ResponseBody
    public CategoryDto updateCategory(@PathVariable Long id,
                                      @Valid @RequestBody CategoryDto categoryDto) {
        log.info("Получен запрос на изменение категории с id: {}, body = {} ",
                id, categoryDto);
        return adminCategoryMapperService.updateCategory(id, categoryDto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("Получен запрос на удаление категории с id {}", id);
        adminCategoryMapperService.delete(id);
    }
}
