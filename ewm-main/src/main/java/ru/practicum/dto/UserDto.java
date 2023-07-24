package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDto {
    @NotNull
    private Long id;

    @Email
    @NotNull
    private String email;

    @NotNull
    @NotBlank
    private String name;

    private Long rating;

}
