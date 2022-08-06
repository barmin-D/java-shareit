package ru.practicum.shareit.user.model;


import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class User {
    private Integer id;
    private String name;
    @NonNull
    @NotBlank
    @Email
    private String email;


}
