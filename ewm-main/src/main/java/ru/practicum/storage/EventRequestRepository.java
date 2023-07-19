package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.enums.EventRequestStatus;
import ru.practicum.model.EventRequest;

import java.util.List;
import java.util.Optional;

public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {

    public List<EventRequest> findByEventIdAndEventInitiatorId(Long eventId, Long initiatorId);

    public List<EventRequest> findByEventIdAndRequesterId(Long eventId, Long requesterId);

    public List<EventRequest> findByRequesterId(Long userId);

    public Optional<EventRequest> findByIdAndRequesterId(Long id, Long userId);

    public Optional<EventRequest> findByIdAndEventIdAndEventInitiatorId(
            Long id, Long eventId, Long initiatorId);

    public long countByEventIdAndStatus(Long eventId, EventRequestStatus eventRequestStatus);
}
