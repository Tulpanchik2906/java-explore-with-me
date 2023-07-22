package ru.practicum.servicies.mapperservicies.forall;

import ru.practicum.CategoryDto;

import java.util.List;

public interface PublicCategoryMapperService {
    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategory(Long id);
}
