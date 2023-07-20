package ru.practicum.model;

import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, length = 200)
    @NotBlank
    @NotNull
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Transient
    @Formula("(select count(l.user_id) " +
            "from LIKES l " +
            "where l.user_id = id AND status = 1)")
    private double rating;
}
