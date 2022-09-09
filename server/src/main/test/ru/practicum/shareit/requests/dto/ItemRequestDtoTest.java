package ru.practicum.shareit.requests.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoTest {
    @Autowired
    private JacksonTester jackson;

    @Test
    void testToString() throws IOException {
        User user = new User(1, "testName2", "test2@email.ru");
        ItemRequestDto itemRequestDto = new ItemRequestDto(100, "tort", new ArrayList<ItemDto>(), user,
                LocalDateTime.now());
        JsonContent<ItemDto> jsonContent = jackson.write(itemRequestDto);

        assertThat(jsonContent).extractingJsonPathNumberValue("@.id").isEqualTo(100);
        assertThat(jsonContent).extractingJsonPathStringValue("@.description").isEqualTo("tort");
    }
}