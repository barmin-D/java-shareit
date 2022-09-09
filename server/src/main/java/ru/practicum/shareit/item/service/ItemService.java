package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.Optional;

public interface ItemService {
    Optional<ItemDto> get(Integer userId, Integer itemId);

    ItemDto addNewItem(Integer userId, ItemDto itemDto);

    void deleteItem(Integer userId, Integer itemId);

    ItemDto update(Integer userId, Integer itemId, ItemDto itemDto);

    Collection<ItemDto> getItems(Integer userId);

    Collection<ItemDto> search(Integer userId, String text);

    CommentDto createComment(Integer userId, Integer itemId, CommentDto commentDto);
}
