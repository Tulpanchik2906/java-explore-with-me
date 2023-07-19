package ru.practicum.services;

import ru.practicum.model.Category;

import java.util.List;

public interface CategoryService {
    public Category create(Category category);

    public Category update(Long categoryId, Category patchCategory);

    public Category get(Long id);

    public List<Category> findAll(Integer from, Integer size);

    public void delete(Long id);
}
