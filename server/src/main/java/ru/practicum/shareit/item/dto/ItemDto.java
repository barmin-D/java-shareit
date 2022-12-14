package ru.practicum.shareit.item.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingBookerDto;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private Integer requestId;
    @JsonIgnore
    private ItemRequest request;
    private BookingBookerDto lastBooking;
    private BookingBookerDto nextBooking;
    private List<CommentDto> comments;
}
