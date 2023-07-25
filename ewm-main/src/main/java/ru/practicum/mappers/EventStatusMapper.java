package ru.practicum.mappers;

import ru.practicum.enums.EventState;
import ru.practicum.enums.StateAction;
import ru.practicum.exception.ValidationException;

public class EventStatusMapper {

    public static EventState toEventStatus(StateAction stateAction) {
        if (stateAction == null) {
            return null;
        }
        switch (stateAction) {
            case PUBLISH_EVENT:
                return EventState.PUBLISHED;
            case SEND_TO_REVIEW:
                return EventState.PENDING;
            case REJECT_EVENT:
                return EventState.REJECTED;
            case CANCEL_REVIEW:
                return EventState.CANCELED;
            default:
                throw new ValidationException(
                        "Соответствие stateAction: " + stateAction.name() +
                                " не найдено");
        }
    }
}
