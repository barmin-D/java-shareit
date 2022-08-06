package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {
    Item get(Integer itemId);

    Item addNewItem(Item item);

    void deleteItem(Integer userId, Integer itemId);

    Collection<Item>  getItems(Integer userId);

    Item update(Item item);

    Collection<Item> search(Integer userId, String text);
}
