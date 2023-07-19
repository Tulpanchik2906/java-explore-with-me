package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
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
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User user;

    @Id
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    @ManyToOne
    private Event event;

    @Column(name = "status")
    private Integer status;
}
