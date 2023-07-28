package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*
    Ключ для таблицы лайков
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeKey implements Serializable {
    private Long userId;

    private Long eventId;
}
