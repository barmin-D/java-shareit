package ru.practicum.shareit.item.dto;


import lombok.*;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;

@Data
public class ItemDto {
    private Integer id;
    private String name;
    private String description;
    @NotNull
    private boolean available;
    private Integer requestCounter;
    private User owner;
    private ItemRequest request;
}
