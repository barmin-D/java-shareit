package ru.practicum.shareit.item.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingBookerDto;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Integer id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private Boolean available;
    private Integer requestCounter;
    private User owner;
    private ItemRequest request;
    private BookingBookerDto lastBooking;
    private BookingBookerDto nextBooking;
    private List<CommentDto> comments;
}
