package ru.practicum.shareit.item.model;


import lombok.*;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private Integer id;
    private String name;
    private String description;
    @NonNull
    private Boolean available;
    private User owner;
    private ItemRequest request;
}
