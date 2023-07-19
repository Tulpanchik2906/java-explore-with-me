package ru.practicum.services.params;

import lombok.Builder;
import lombok.Data;
import ru.practicum.model.Location;

@Data
@Builder
public class CreateEventParam {
    private Long userId;

    private Long categoryId;

    private Location location;

}
