package ru.practicum.model;

import lombok.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
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

    @Column(length = 7000)
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

    @NotBlank
    private String title;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EventState state;

    @NotNull
    private Long views;

    private Long rating;
}
