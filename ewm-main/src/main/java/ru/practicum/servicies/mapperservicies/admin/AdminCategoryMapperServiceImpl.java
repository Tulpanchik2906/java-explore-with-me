package ru.practicum.servicies.mapperservicies.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.CategoryDto;
import ru.practicum.NewCategoryDto;
import ru.practicum.mappers.CategoryMapper;
import ru.practicum.servicies.logicservicies.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminCategoryMapperServiceImpl
        implements AdminCategoryMapperService {

    private final CategoryService categoryService;

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        return categoryService.findAll(from, size).stream()
                .map(category -> CategoryMapper.INSTANCE.toCategoryDto(category))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        return CategoryMapper.INSTANCE.toCategoryDto(categoryService.create(
                CategoryMapper.INSTANCE.toCategory(newCategoryDto)));
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        return CategoryMapper.INSTANCE.toCategoryDto(
                categoryService.update(id, CategoryMapper.INSTANCE.toCategory(categoryDto)));
    }

    @Override
    public void delete(Long id) {
        categoryService.delete(id);
    }
}
