package ru.practicum.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class LikeKey implements Serializable {
    private Long userId;

    private Long eventId;
}
