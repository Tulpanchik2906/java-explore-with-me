package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

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
    private Long userId;

    @Id
    @Column(name = "event_id")
    private Long eventId;

    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne
    private User user;

    @Id
    @JoinColumn(name = "event_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne
    private Event event;

    @NotNull
    private Integer status;
}
