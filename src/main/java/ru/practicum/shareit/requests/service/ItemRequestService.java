package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.util.Collection;


public interface ItemRequestService {
    ItemRequestDto add(Integer userId, ItemRequestDto itemRequestDto);

    Collection<ItemRequestDto> get(Integer userId);

    Collection<ItemRequestDto> getAll(Integer userId, Integer from, Integer size);

    ItemRequestDto getById(Integer userId, Integer requestId);
}
