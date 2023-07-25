package ru.practicum.mappers;

import ru.practicum.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.servicies.params.EventRequestStatusUpdateResult;

import java.util.stream.Collectors;

public class EventRequestStatusUpdateResultMapper {

    public static EventRequestStatusUpdateResultDto eventRequestStatusUpdateResultDto(
            EventRequestStatusUpdateResult eventRequestStatusUpdateResult) {
        return EventRequestStatusUpdateResultDto.builder()
                .confirmedRequests(
                        eventRequestStatusUpdateResult.getConfirmedRequests().stream()
                                .map(x -> EventRequestMapper.INSTANCE.toEventParticipationRequestDto(x))
                                .collect(Collectors.toList()))
                .rejectedRequests(
                        eventRequestStatusUpdateResult.getRejectedRequests().stream()
                                .map(x -> EventRequestMapper.INSTANCE.toEventParticipationRequestDto(x))
                                .collect(Collectors.toList()))
                .build();
    }
}
