package ru.practicum.servicies.logicservicies;

import ru.practicum.model.Event;
import ru.practicum.servicies.params.CreateEventParam;
import ru.practicum.servicies.params.PatchEventParam;
import ru.practicum.servicies.params.SearchEventParamForAdmin;
import ru.practicum.servicies.params.SearchEventParamForUser;

import java.util.List;

public interface EventService {

    public Event create(Event event, CreateEventParam createEventParam);

    public List<Event> findAllByInitiatorId(Long userId, int from, int size);

    public Event getEventByEventIdAndInitiatorId(Long eventId, Long userId);

    public Event findPublicEvent(Long eventId);

    public Event patchByUser(Event patchEvent, PatchEventParam patchEventParam);

    public Event patchByAdmin(Event patchEvent, PatchEventParam patchEventParam);

    public List<Event> findAllForUser(SearchEventParamForUser searchEventParamForUser);

    public List<Event> findAllForAdmin(SearchEventParamForAdmin searchEventParamForAdmin);

    public void saveNewView(Long eventId);
}
