package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDto.ItemDtoBuilder;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.Item.ItemBuilder;

@Component
public class ItemMapperImpl implements ItemMapper {

    @Override
    public ItemDto toItemDto(Item item) {
        if (item == null) {
            return null;
        }
        ItemDtoBuilder itemDto = ItemDto.builder();
        itemDto.id(item.getId());
        itemDto.name(item.getName());
        itemDto.description(item.getDescription());
        itemDto.available(item.getAvailable());
        itemDto.owner(item.getOwner());
        itemDto.request(item.getRequest());
        return itemDto.build();
    }

    @Override
    public Item toItem(ItemDto itemDto) {
        if (itemDto == null) {
            return null;
        }
        ItemBuilder item = Item.builder();
        item.id(itemDto.getId());
        item.name(itemDto.getName());
        item.description(itemDto.getDescription());
        item.available(itemDto.getAvailable());
        item.owner(itemDto.getOwner());
        item.request(itemDto.getRequest());
        return item.build();
    }

    @Override
    public void updateItemFromItemDto(ItemDto itemDto, Item item) {
        if (itemDto == null) {
            return;
        }
        if (itemDto.getId() != null) {
            item.setId(itemDto.getId());
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getOwner() != null) {
            item.setOwner(itemDto.getOwner());
        }
        if (itemDto.getRequest() != null) {
            item.setRequest(itemDto.getRequest());
        }
    }
}
