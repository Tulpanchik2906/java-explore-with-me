package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class NewUserRequest {

    @Email
    @NotNull
    @Length(min = 6, max = 254)
    private String email;

    @NotNull
    @NotBlank
    @Length(min = 2, max = 250)
    private String name;

}
