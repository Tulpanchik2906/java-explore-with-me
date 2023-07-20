package ru.practicum.servicies.params;

import lombok.Builder;
import lombok.Data;
import ru.practicum.model.EventRequest;

import java.util.List;

@Builder
@Data
public class EventRequestStatusUpdateResult {
    private List<EventRequest> confirmedRequests;
    private List<EventRequest> rejectedRequests;
}
