package ru.practicum.servicies.mapperservicies;

import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;

import java.util.List;


public interface AdminCategoryMapperService {
    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    CategoryDto updateCategory(Long id, CategoryDto categoryDto);

    void delete(Long id);
}
