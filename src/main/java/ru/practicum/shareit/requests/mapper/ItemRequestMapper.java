package ru.practicum.shareit.requests.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

public interface ItemRequestMapper {
    ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<ItemDto> list);

    ItemRequest toItemRequest(ItemRequestDto itemRequestDto);
}
