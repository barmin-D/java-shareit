package ru.practicum.shareit.requests.mapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Rollback(false)
@SpringBootTest(
        properties = "db.name=test7",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestMapperImplTest {
    @Autowired
    ItemRequestMapper itemRequestMapper;
    private ItemRequestDto itemRequestDto;
    private ItemRequest itemRequest;
    private User user;

    @Test
    void toItemRequestDto() {
        itemRequest = new ItemRequest(1, "test", user, LocalDateTime.now());
        itemRequestDto = itemRequestMapper.toItemRequestDto(itemRequest, new ArrayList<>());
        assertEquals(itemRequestDto.getDescription(), itemRequest.getDescription());
        assertEquals(itemRequestDto.getRequestor(), itemRequest.getRequestor());
    }

    @Test
    void toItemRequest() {
        itemRequestDto = new ItemRequestDto(1, "test", new ArrayList<ItemDto>(), user,
                LocalDateTime.now());
        itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
        assertEquals(itemRequest.getRequestor(), itemRequestDto.getRequestor());
    }
}