package ru.practicum.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    public List<Event> findByInitiatorId(Long userId);

    public List<Event> findByInitiatorId(Long userId, Pageable pageable);

    public Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    public List<Event> findByIdIn(List<Long> ids);

    public Optional<Event> findByIdAndPublishedOnIsNotNull(Long eventId);
}
