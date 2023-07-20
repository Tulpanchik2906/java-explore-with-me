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
        if (pinned != null) {
            BooleanExpression byPinned = QCompilation.compilation.pinned.eq(pinned);
            int startPage = PageUtil.getStartPage(from, size);

            if (PageUtil.isTwoSite(from, size)) {
                List<Compilation> list = compilationRepository
                        .findAll(byPinned, PageRequest.of(startPage, size)).stream()
                        .collect(Collectors.toList());

                list.addAll(compilationRepository.findAll(byPinned, PageRequest.of(startPage + 1, size))
                        .stream().collect(Collectors.toList()));

                return PageUtil.getPageListForTwoPage(list,
                        PageUtil.getStartFrom(from, size), size);
            } else {
                return compilationRepository.findAll(byPinned, PageRequest.of(startPage, size))
                        .stream().limit(size)
                        .collect(Collectors.toList());
            }
        } else {
            int startPage = PageUtil.getStartPage(from, size);

            if (PageUtil.isTwoSite(from, size)) {
                List<Compilation> list = (compilationRepository.findAll(PageRequest.of(startPage, size))
                        .stream().collect(Collectors.toList()));
                list.addAll(compilationRepository.findAll(PageRequest.of(startPage + 1, size))
                        .stream().collect(Collectors.toList()));

                return PageUtil.getPageListForTwoPage(list,
                        PageUtil.getStartFrom(from, size), size);
            } else {
                return compilationRepository.findAll(PageRequest.of(startPage, size))
                        .stream().limit(size)
                        .collect(Collectors.toList());
            }
        }
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
            if (compilation.getTitle().isBlank()) {
                throw new ValidationException("Название подборки не может состоять только из пробелов");
            }
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
}
