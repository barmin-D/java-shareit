package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoTest {
    @Autowired
    private JacksonTester jackson;

    @Test
    void builder() throws IOException {
        User user = new User(1, "testName2", "test2@email.ru");
        ItemDto itemDto = new ItemDto(1, "name", "desc", true, user, 1, null,
                null, null, new ArrayList<>());
        JsonContent<ItemDto> jsonContent = jackson.write(itemDto);

        assertThat(jsonContent).extractingJsonPathNumberValue("@.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("@.name").isEqualTo("name");
        assertThat(jsonContent).extractingJsonPathStringValue("@.description").isEqualTo("desc");
    }
}