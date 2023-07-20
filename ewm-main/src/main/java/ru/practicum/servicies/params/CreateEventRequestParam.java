package ru.practicum.servicies.params;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateEventRequestParam {

    public Long userId;
    public Long eventId;
}
