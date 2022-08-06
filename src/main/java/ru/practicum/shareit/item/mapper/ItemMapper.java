package ru.practicum.shareit.item.mapper;

import org.mapstruct.MappingTarget;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public interface ItemMapper {
    ItemDto toItemDto(Item item);

    Item toItem(ItemDto itemDto);

    void updateItemFromItemDto(ItemDto itemDto, @MappingTarget Item item);
}

