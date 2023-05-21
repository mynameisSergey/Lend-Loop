package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class UserDto {
    private int id;
    @NotEmpty
    private String name;
    @NotEmpty
    @Email(message = "Некорректный email")
    private String email;
}