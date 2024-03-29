package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.annotation.NotBlankWithNull;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UpdateCompilationRequest {
    private Set<Long> events;

    private Boolean pinned;

    @Length(min = 1, max = 50)
    @NotBlankWithNull
    private String title;
}
