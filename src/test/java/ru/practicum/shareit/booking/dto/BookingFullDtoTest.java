package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingFullDtoTest {
    @Autowired
    private JacksonTester jackson;

    @Test
    void builder() throws IOException {
        User user = new User(1, "testName2", "test2@email.ru");
        Item item = new Item(1, "name", "test", true, user, null);

        BookingFullDto bookingFullDto = new BookingFullDto(1, LocalDateTime.now(), LocalDateTime.now(),
                item, user, Status.WAITING);
        JsonContent<BookingFullDto> jsonContent = jackson.write(bookingFullDto);
        assertThat(jsonContent).extractingJsonPathNumberValue("@.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathNumberValue("@.item.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathNumberValue("@.booker.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("@.status").isEqualTo("WAITING");
    }
}