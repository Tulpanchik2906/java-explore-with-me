package ru.practicum.model;

import lombok.*;
import org.hibernate.annotations.Formula;
import ru.practicum.enums.EventState;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "annotation", length = 2000)
    @NotBlank
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @NotNull
    private Category category;

    @Transient
    private Long confirmedRequests;

    @Column(name = "created_on")
    @NotNull
    private LocalDateTime createdOn;

    @Column(name = "description", length = 7000)
    @NotBlank
    private String description;

    @Column(name = "event_date")
    @NotNull
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    @NotNull
    private User initiator;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    @NotNull
    private Location location;

    @Column(name = "paid")
    @NotNull
    private Boolean paid;

    @Column(name = "participant_limit")
    @NotNull
    private Long participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    @NotNull
    private Boolean requestModeration;

    @Column(name = "title")
    @NotBlank
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    @NotNull
    private EventState state;

    @Column(name = "views")
    @NotNull
    private Long views;

    @Transient
    @Formula("(select count(l.event_id) " +
            "from LIKES l " +
            "where l.event_id = id AND status = 1)")
    private double rating;
}
