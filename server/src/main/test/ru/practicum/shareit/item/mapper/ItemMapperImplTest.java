package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Rollback(false)
@SpringBootTest(
        properties = "db.name=test7",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemMapperImplTest {
    @Autowired
    private ItemMapper itemMapper;
    private Item item;
    private User user;
    private ItemDto itemDto;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        user = new User(1, "testName2", "test2@email.ru");
        itemDto = new ItemDto(null, "name", "description", true,
                null, null, null, null,
                null, new ArrayList<>());
        item = new Item(1, "name", "test", true, user, null);
        commentDto = new CommentDto(null, "trololo", null, null);
    }

    @Test
    void toItemDto() {
        itemDto = itemMapper.toItemDto(item);

        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
    }

    @Test
    void toItem() {
        item = itemMapper.toItem(itemDto);

        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
    }

    @Test
    void updateItemFromItemDto() {
        itemMapper.updateItemFromItemDto(itemDto, item);
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
    }

    @Test
    void toItemFullDto() {
        itemDto = itemMapper.toItemFullDto(item, null, null, List.of(commentDto));
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
    }
}