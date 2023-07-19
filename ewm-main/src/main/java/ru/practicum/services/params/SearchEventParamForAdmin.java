package ru.practicum.services.params;

import lombok.Builder;
import lombok.Data;
import ru.practicum.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SearchEventParamForAdmin {

    private List<Long> users;

    private List<EventState> states;

    private List<Long> categories;

    private LocalDateTime rangeStart;

    private LocalDateTime rangeEnd;

    private Integer from;

    private Integer size;

}
