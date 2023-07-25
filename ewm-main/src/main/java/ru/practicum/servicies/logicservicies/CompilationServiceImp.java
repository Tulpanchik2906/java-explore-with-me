package ru.practicum.servicies.logicservicies;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.model.QCompilation;
import ru.practicum.storage.CompilationRepository;
import ru.practicum.storage.EventRepository;
import ru.practicum.util.PageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImp implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Compilation> findAll(Boolean pinned, int from, int size) {
        List<Compilation> list = null;
        if (pinned != null) {
            BooleanExpression byPinned = QCompilation.compilation.pinned.eq(pinned);
            int startPage = PageUtil.getStartPage(from, size);

            list = findAllByPageWithQuery(startPage, size, byPinned);

            if (PageUtil.isTwoSite(from, size)) {
                list.addAll(findAllByPageWithQuery(startPage + 1, size, byPinned));
            }
        } else {
            int startPage = PageUtil.getStartPage(from, size);

            list = findAllByPageWithOutQuery(startPage, size);

            if (PageUtil.isTwoSite(from, size)) {
                list.addAll(findAllByPageWithOutQuery(startPage + 1, size));

            }
        }
        return PageUtil.getPageListByPage(list,
                PageUtil.getStartFrom(from, size), size);
    }

    @Override
    @Transactional(readOnly = true)
    public Compilation get(Long id) {
        return compilationRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Подборка с id: {} не найдена."));
    }

    @Override
    @Transactional
    public Compilation create(Compilation compilation, Set<Long> eventIds) {
        if (eventIds != null) {
            List<Event> events = eventRepository.findByIdIn(new ArrayList<>(eventIds));
            compilation.setEvents(events);
        }
        if (compilation.getTitle() != null && compilation.getTitle().isBlank()) {
            throw new ValidationException("Название подборки не может состоять только из пробелов");
        }
        return compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public Compilation update(Long compId, Compilation compilation,
                              Set<Long> eventIds) {
        Compilation oldCompilation = get(compId);
        if (eventIds != null) {
            oldCompilation.setEvents(
                    eventRepository.findByIdIn(new ArrayList<>(eventIds)));
        }
        if (compilation.getPinned() != null) {
            oldCompilation.setPinned(compilation.getPinned());
        }
        if (compilation.getTitle() != null) {
            oldCompilation.setTitle(compilation.getTitle());
        }
        return compilationRepository.save(oldCompilation);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        get(id);
        compilationRepository.deleteById(id);
    }

    private List<Compilation> findAllByPageWithOutQuery(int startPage, int size) {
        return compilationRepository.findAll(PageRequest.of(startPage, size))
                .stream().collect(Collectors.toList());
    }

    private List<Compilation> findAllByPageWithQuery(int startPage, int size, BooleanExpression query) {
        return compilationRepository.findAll(query, PageRequest.of(startPage, size))
                .stream().collect(Collectors.toList());
    }
}
