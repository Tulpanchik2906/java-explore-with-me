package ru.practicum.servicies.logicservicies;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
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
    @Transactional
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category update(Long categoryId, Category patchCategory) {
        Category oldCategory = get(categoryId);
        if (patchCategory.getName() != null) {
            oldCategory.setName(patchCategory.getName());
        }
        return categoryRepository.save(oldCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public Category get(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        "Категория с id: " + id + " не найдена."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll(Integer from, Integer size) {
        int startPage = PageUtil.getStartPage(from, size);

        List<Category> list = findAllByPage(startPage, size);

        if (PageUtil.isTwoSite(from, size)) {
            list.addAll(findAllByPage(startPage + 1, size));

        }

        return PageUtil.getPageListByPage(list,
                PageUtil.getStartFrom(from, size), size);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    private List<Category> findAllByPage(int startPage, int size) {
        return categoryRepository.findAll(PageRequest.of(startPage, size))
                .stream().collect(Collectors.toList());
    }
}
