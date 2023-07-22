package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class NewCompilationDto {
    private Set<Long> events;

    private Boolean pinned = false;

    @NotNull
    @Length(min = 1, max = 50)
    @NotBlank
    private String title;
}
