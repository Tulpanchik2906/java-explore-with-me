package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/*
    Модель лайка
 */
@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "likes")
@IdClass(LikeKey.class)
public class Like implements Serializable {

    @Id
    @Column(name = "user_id")
    private Long userId; // колонка для ключа

    @Id
    @Column(name = "event_id")
    private Long eventId; // колонка для ключа

    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne
    private User user; // колонка для объекта

    @Id
    @JoinColumn(name = "event_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne
    private Event event; // колонка для события

    @NotNull
    private Integer status; // колонка статус (1-лайк, -1 - дизлайк)
}
