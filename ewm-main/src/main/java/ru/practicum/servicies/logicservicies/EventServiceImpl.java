package ru.practicum.servicies.logicservicies;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatClient;
import ru.practicum.dto.ViewStats;
import ru.practicum.enums.EventRequestStatus;
import ru.practicum.enums.EventSort;
import ru.practicum.enums.EventState;
import ru.practicum.exception.NotAvailableException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.QEvent;
import ru.practicum.model.User;
import ru.practicum.servicies.params.CreateEventParam;
import ru.practicum.servicies.params.PatchEventParam;
import ru.practicum.servicies.params.SearchEventParamForAdmin;
import ru.practicum.servicies.params.SearchEventParamForUser;
import ru.practicum.storage.CategoryRepository;
import ru.practicum.storage.EventRepository;
import ru.practicum.storage.EventRequestRepository;
import ru.practicum.storage.UserRepository;
import ru.practicum.util.DateTimeFormatterUtil;
import ru.practicum.util.PageUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRequestRepository eventRequestRepository;
    private final StatClient statClient;

    @Override
    @Transactional
    public Event create(Event event, CreateEventParam createEventParam) {
        User initiator = getUser(createEventParam.getUserId());
        Category category = getCategory(createEventParam.getCategoryId());

        event.setInitiator(initiator);
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        event.setLocation(createEventParam.getLocation());
        event.setViews(0L);
        event.setRating(0L);

        return eventRepository.save(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> findAllByInitiatorId(Long userId, int from, int size) {
        int startPage = PageUtil.getStartPage(from, size);
        if (PageUtil.isTwoSite(from, size)) {
            List<Event> list = eventRepository.findByInitiatorId(userId,
                    PageRequest.of(startPage, size));

            list.addAll(eventRepository.findByInitiatorId(userId,
                    PageRequest.of(startPage + 1, size)));

            return PageUtil.getPageListByPage(list,
                    PageUtil.getStartFrom(from, size), size);
        } else {
            return eventRepository.findByInitiatorId(userId,
                            PageRequest.of(startPage, size))
                    .stream().limit(size)
                    .collect(Collectors.toList());
        }

    }

    @Override
    @Transactional(readOnly = true)
    public Event getEventByEventIdAndInitiatorId(Long eventId, Long userId) {
        return eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(
                        "Не найдено событие с id: " + eventId +
                                " у пользователя с id: " + userId));
    }

    @Override
    @Transactional(readOnly = true)
    public Event findPublicEvent(Long eventId) {
        Event res = eventRepository.findByIdAndPublishedOnIsNotNull(eventId)
                .orElseThrow(() ->
                        new NotFoundException("Событие с id: "
                                + eventId + " не найдено среди опубликованных"));

        res.setViews(getCountViewsForEvent(res));
        res.setConfirmedRequests(eventRequestRepository
                .countByEventIdAndStatus(eventId, EventRequestStatus.CONFIRMED));

        return res;
    }

    @Override
    @Transactional
    public Event patchByUser(Event patchEvent, PatchEventParam patchEventParam) {
        Event oldEvent = getEventByEventIdAndInitiatorId(patchEventParam.getEventId(), patchEventParam.getUserId());

        if (oldEvent.getState() == EventState.PUBLISHED) {
            throw new NotAvailableException("Пользователь не может изменять уже опубликованное событие.");
        } else {
            if (patchEventParam.getEventState() != null) {
                oldEvent.setState(patchEventParam.getEventState());
            }
        }

        return eventRepository.save(updateEvent(oldEvent, patchEvent, patchEventParam));
    }

    @Override
    @Transactional
    public Event patchByAdmin(Event patchEvent, PatchEventParam patchEventParam) {
        Event oldEvent = getEvent(patchEventParam.getEventId());
        oldEvent = updateEvent(oldEvent, patchEvent, patchEventParam);
        if (patchEventParam.getEventState() != null) {
            if (oldEvent.getState() == EventState.PENDING) {
                oldEvent.setState(patchEventParam.getEventState());
                if (patchEventParam.getEventState() == EventState.PUBLISHED) {
                    oldEvent.setPublishedOn(LocalDateTime.now());
                }
            } else {
                throw new NotAvailableException("Нельзя опубликовать событие из состояния: " +
                        oldEvent.getState());
            }
        }

        return eventRepository.save(oldEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> findAllForUser(SearchEventParamForUser searchEventParamForUser) {
        BooleanExpression query = null;

        if (searchEventParamForUser.getText() != null) {
            BooleanExpression byText = QEvent.event.annotation.containsIgnoreCase(searchEventParamForUser.getText())
                    .or(QEvent.event.description.containsIgnoreCase(searchEventParamForUser.getText()));

            query = byText;
        }

        if (searchEventParamForUser.getCategories() != null) {
            BooleanExpression byCategories = QEvent.event.category.in(
                    categoryRepository.findByIdIn(searchEventParamForUser.getCategories()));

            if (query == null) {
                query = byCategories;
            } else {
                query = query.and(byCategories);
            }
        }

        if (searchEventParamForUser.getPaid() != null) {
            BooleanExpression byPaid = QEvent.event.paid.eq(searchEventParamForUser.getPaid());
            if (query == null) {
                query = byPaid;
            } else {
                query = query.and(byPaid);
            }
        }

        if (searchEventParamForUser.getRangeStart() != null) {
            BooleanExpression byStart = QEvent.event.eventDate
                    .after(searchEventParamForUser.getRangeStart());

            if (query == null) {
                query = byStart;
            } else {
                query = query.and(byStart);
            }
        }

        if (searchEventParamForUser.getRangeEnd() != null) {
            BooleanExpression byEnd = QEvent.event.eventDate
                    .before(searchEventParamForUser.getRangeEnd());

            if (query == null) {
                query = byEnd;
            } else {
                query = query.and(byEnd);
            }
        }

        if (searchEventParamForUser.getOnlyAvailable()) {
            BooleanExpression byAwail = QEvent.event.state.eq(EventState.PUBLISHED);

            if (query == null) {
                query = byAwail;
            } else {
                query = query.and(byAwail);
            }
        }

        Sort sort = null;

        if (searchEventParamForUser.getSort() != null) {
            switch (EventSort.valueOf(searchEventParamForUser.getSort())) {
                case VIEWS:
                    sort = Sort.by("views").descending();
                    break;
                case EVENT_DATE:
                    sort = Sort.by("eventDate");
                    break;
                case RATING:
                    sort = Sort.by("rating").descending();
            }
        }

        List<Event> res = getListForUserQuery(searchEventParamForUser, query, sort);
        res.forEach(x -> x.setConfirmedRequests(
                eventRequestRepository.countByEventIdAndStatus(x.getId(), EventRequestStatus.CONFIRMED)));
        return res;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> findAllForAdmin(SearchEventParamForAdmin searchEventParamForAdmin) {
        BooleanExpression query = null;
        if (searchEventParamForAdmin.getUsers() != null) {
            query = QEvent.event.initiator.in(
                    userRepository.findByIdIn(searchEventParamForAdmin.getUsers()));
        }
        if (searchEventParamForAdmin.getStates() != null) {
            BooleanExpression byStates = QEvent.event.state.in(searchEventParamForAdmin.getStates());
            if (query == null) {
                query = byStates;
            } else {
                query = query.and(byStates);
            }
        }

        if (searchEventParamForAdmin.getCategories() != null) {
            BooleanExpression byCategories = QEvent.event.category.in(
                    categoryRepository.findByIdIn(searchEventParamForAdmin.getCategories()));

            if (query == null) {
                query = byCategories;
            } else {
                query = query.and(byCategories);
            }
        }


        if (searchEventParamForAdmin.getRangeStart() != null) {
            BooleanExpression byStart = QEvent.event.eventDate
                    .after(searchEventParamForAdmin.getRangeStart());

            if (query == null) {
                query = byStart;
            } else {
                query = query.and(byStart);
            }
        }

        if (searchEventParamForAdmin.getRangeEnd() != null) {
            BooleanExpression byEnd = QEvent.event.eventDate
                    .before(searchEventParamForAdmin.getRangeEnd());

            if (query == null) {
                query = byEnd;
            } else {
                query = query.and(byEnd);
            }
        }

        List<Event> res = getListForAdmin(searchEventParamForAdmin, query);
        res.forEach(x -> x.setConfirmedRequests(
                eventRequestRepository.countByEventIdAndStatus(x.getId(), EventRequestStatus.CONFIRMED)));
        return res;
    }

    @Override
    @Transactional
    public void saveNewView(Long eventId) {
        Event event = getEvent(eventId);
        long views = getCountViewsForEvent(event);
        event.setViews(views);

        eventRepository.save(event);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(
                        "Пользователь с id: " + userId + " не найден."));
    }

    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException(
                        "Категория с id: " + categoryId + " не найдена."));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(
                        "Событие с id: " + eventId + " не найдена."));
    }

    private Event updateEvent(Event oldEvent, Event patchEvent, PatchEventParam patchEventParam) {
        if (patchEvent.getAnnotation() != null) {
            oldEvent.setAnnotation(patchEvent.getAnnotation());
        }

        if (patchEventParam.getCategoryId() != null) {
            Category category = getCategory(patchEventParam.getCategoryId());
            oldEvent.setCategory(category);
        }

        if (patchEvent.getDescription() != null) {
            oldEvent.setDescription(patchEvent.getDescription());
        }

        if (patchEvent.getEventDate() != null) {
            oldEvent.setEventDate(patchEvent.getEventDate());
        }

        if (patchEvent.getLocation() != null) {
            oldEvent.setLocation(patchEvent.getLocation());
        }

        if (patchEvent.getPaid() != null) {
            oldEvent.setPaid(patchEvent.getPaid());
        }

        if (patchEvent.getParticipantLimit() != null) {
            oldEvent.setParticipantLimit(patchEvent.getParticipantLimit());
        }

        if (patchEvent.getRequestModeration() != null) {
            oldEvent.setRequestModeration(patchEvent.getRequestModeration());
        }

        if (patchEvent.getTitle() != null) {
            oldEvent.setTitle(patchEvent.getTitle());
        }

        return oldEvent;
    }

    private Long getCountViewsForEvent(Event event) {
        String uris = "/events/" + event.getId();

        Map<String, Object> parameters = Map.of(
                "start", event.getCreatedOn().minusDays(3).format(
                        DateTimeFormatter.ofPattern(DateTimeFormatterUtil.DATE_TIME_FORMATTER)),
                "end", LocalDateTime.now().plusDays(3).format(
                        DateTimeFormatter.ofPattern(DateTimeFormatterUtil.DATE_TIME_FORMATTER)),
                "uris", uris,
                "unique", true);

        List<ViewStats> list = statClient.getViewStats(parameters).getBody();
        if (list.isEmpty()) {
            return 0L;
        } else {
            return list.get(0).getHits();
        }
    }

    private List<Event> getListForUserQuery(
            SearchEventParamForUser searchEventParamForUser, BooleanExpression query, Sort sort) {
        int startPage = PageUtil.getStartPage(
                searchEventParamForUser.getFrom(), searchEventParamForUser.getSize());
        List<Event> list = null;
        if (query != null && sort != null) {
            list = findAllWithQueryAndSort(
                    startPage, searchEventParamForUser.getSize(), query, sort);

            if (PageUtil.isTwoSite(searchEventParamForUser.getFrom(), searchEventParamForUser.getSize())) {
                list.addAll(findAllWithQueryAndSort(
                        startPage + 1, searchEventParamForUser.getSize(), query, sort));
            }
        }

        if (query != null && sort == null) {
            list = findAllWithQueryAndWithOutSort(
                    startPage, searchEventParamForUser.getSize(), query);

            if (PageUtil.isTwoSite(searchEventParamForUser.getFrom(), searchEventParamForUser.getSize())) {
                list.addAll(findAllWithQueryAndWithOutSort(
                        startPage + 1, searchEventParamForUser.getSize(), query));
            }
        }

        if (query == null && sort != null) {
            list = findAllWithSortAndWithOutQuery(
                    startPage, searchEventParamForUser.getSize(), sort);

            if (PageUtil.isTwoSite(searchEventParamForUser.getFrom(), searchEventParamForUser.getSize())) {
                list.addAll(findAllWithSortAndWithOutQuery(
                        startPage + 1, searchEventParamForUser.getSize(), sort));
            }
        }

        if (query == null && sort == null) {
            list = findAllWithOutQueryAndSort(startPage, searchEventParamForUser.getSize());

            if (PageUtil.isTwoSite(searchEventParamForUser.getFrom(), searchEventParamForUser.getSize())) {
                list.addAll(findAllWithOutQueryAndSort(
                        startPage + 1, searchEventParamForUser.getSize()));
            }
        }

        return PageUtil.getPageListByPage(list,
                PageUtil.getStartFrom(searchEventParamForUser.getFrom(),
                        searchEventParamForUser.getSize()), searchEventParamForUser.getSize());
    }

    private List<Event> getListForAdmin(
            SearchEventParamForAdmin searchEventParamForAdmin, BooleanExpression query) {

        int startPage = PageUtil.getStartPage(
                searchEventParamForAdmin.getFrom(), searchEventParamForAdmin.getSize());
        List<Event> list;
        if (query != null) {
            list = findAllWithQueryAndWithOutSort(startPage, searchEventParamForAdmin.getSize(), query);

            if (PageUtil.isTwoSite(searchEventParamForAdmin.getFrom(),
                    searchEventParamForAdmin.getSize())) {
                list.addAll(findAllWithQueryAndWithOutSort(
                        startPage + 1, searchEventParamForAdmin.getSize(), query));
            }
        } else {
            list = findAllWithOutQueryAndSort(startPage, searchEventParamForAdmin.getSize());

            if (PageUtil.isTwoSite(searchEventParamForAdmin.getFrom(),
                    searchEventParamForAdmin.getSize())) {
                list.addAll(findAllWithOutQueryAndSort(
                        startPage + 1, searchEventParamForAdmin.getSize()));
            }
        }
        return PageUtil.getPageListByPage(list,
                PageUtil.getStartFrom(searchEventParamForAdmin.getFrom(),
                        searchEventParamForAdmin.getSize()), searchEventParamForAdmin.getSize());
    }

    private List<Event> findAllWithOutQueryAndSort(int startPage, int size) {
        return eventRepository.findAll(PageRequest.of(startPage, size)).stream()
                .collect(Collectors.toList());
    }

    private List<Event> findAllWithQueryAndWithOutSort(int startPage, int size, BooleanExpression query) {
        return eventRepository.findAll(query, PageRequest.of(startPage, size)).stream()
                .collect(Collectors.toList());
    }

    private List<Event> findAllWithSortAndWithOutQuery(int startPage, int size, Sort sort) {
        return eventRepository.findAll(PageRequest.of(startPage, size, sort)).stream()
                .collect(Collectors.toList());
    }

    private List<Event> findAllWithQueryAndSort(
            int startPage, int size, BooleanExpression query, Sort sort) {
        return eventRepository.findAll(query, PageRequest.of(startPage, size, sort)).stream()
                .collect(Collectors.toList());
    }
}


