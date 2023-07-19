package ru.practicum.services.params;

import lombok.Builder;
import lombok.Data;
import ru.practicum.enums.EventState;
import ru.practicum.model.Location;

@Data
@Builder
public class PatchEventParam {
    private Long userId;

    private Long eventId;

    private Long categoryId;

    private EventState eventState;

    private Location location;

}
