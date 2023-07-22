package ru.practicum.servicies.mapperservicies.forall;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.CategoryDto;
import ru.practicum.mappers.CategoryMapper;
import ru.practicum.servicies.logicservicies.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCategoryMapperServiceImpl implements PublicCategoryMapperService {
    private final CategoryService categoryService;

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        return categoryService.findAll(from, size).stream()
                .map(CategoryMapper.INSTANCE::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(Long id) {
        return CategoryMapper.INSTANCE.toCategoryDto(categoryService.get(id));
    }
}
