package ru.practicum.shareit.requests.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ItemRequestMapperImpl implements ItemRequestMapper {
    @Override
    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<ItemDto> list) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .items(new ArrayList<>(list))
                .created(itemRequest.getCreated())
                .build();
    }

    @Override
    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .requestor(itemRequestDto.getRequestor())
                .created(LocalDateTime.now().withNano(0))
                .build();
    }
}
