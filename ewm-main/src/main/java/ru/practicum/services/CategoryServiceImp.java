package ru.practicum.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.model.Category;
import ru.practicum.storage.CategoryRepository;
import ru.practicum.util.PageUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Long categoryId, Category patchCategory) {
        Category oldCategory = get(categoryId);
        if (patchCategory.getName() != null) {
            if (patchCategory.getName().isBlank()) {
                throw new ValidationException("Поле имя не может состоять из одних пробелов.");
            }
            oldCategory.setName(patchCategory.getName());
        }
        return categoryRepository.save(oldCategory);
    }

    @Override
    public Category get(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        "Категория с id: " + id + " не найдена."));
    }

    @Override
    public List<Category> findAll(Integer from, Integer size) {
        if (from == null && size == null) {
            return categoryRepository.findAll();
        } else if (from == null || size == null) {
            throw new ValidationException("Не хватает параметров для формирования списка");
        } else {
            // Получить номер страницы, с которой взять данные
            int startPage = PageUtil.getStartPage(from, size);

            if (PageUtil.isTwoSite(from, size)) {
                // Получить данные с первой страницы
                List<Category> list = (List) categoryRepository.findAll(PageRequest.of(startPage, size));
                // Получить данные со второй страницы
                list.addAll((List) categoryRepository.findAll(PageRequest.of(startPage + 1, size)));
                // Отсечь лишние данные сверху удалением из листа до нужного id,
                // а потом сделать отсечение через функцию limit
                return PageUtil.getPageListForTwoPage(list,
                        PageUtil.getStartFrom(from, size), size);
            } else {
                return categoryRepository.findAll(PageRequest.of(startPage, size))
                        .stream().limit(size)
                        .collect(Collectors.toList());
            }
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
