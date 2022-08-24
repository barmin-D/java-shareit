package ru.practicum.shareit.item.mapper;

import org.mapstruct.MappingTarget;
import ru.practicum.shareit.booking.dto.BookingBookerDto;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemMapper {
    ItemDto toItemDto(Item item);

    Item toItem(ItemDto itemDto);

    void updateItemFromItemDto(ItemDto itemDto, @MappingTarget Item item);

    ItemDto toItemFullDto(Item item, BookingBookerDto last, BookingBookerDto first, List<CommentDto> commentDtoList);
}

