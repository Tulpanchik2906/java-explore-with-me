package ru.practicum.servicies.logicservicies;

import ru.practicum.model.Compilation;

import java.util.List;
import java.util.Set;

public interface CompilationService {

    public List<Compilation> findAll(Boolean pinned, int from, int size);

    public Compilation get(Long id);

    public Compilation create(Compilation compilation, Set<Long> eventIds);

    public Compilation update(
            Long compId, Compilation compilation, Set<Long> eventIds);

    public void delete(Long id);

}
