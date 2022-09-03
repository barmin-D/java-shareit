package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.status.Status;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> jackson;

    @Test
    void builder() throws IOException {
        BookingDto bookingDto = new BookingDto(1, 2, 1, LocalDateTime.now(),
                LocalDateTime.now().plusHours(1), Status.WAITING);
        JsonContent<BookingDto> jsonContent = jackson.write(bookingDto);
        assertThat(jsonContent).extractingJsonPathNumberValue("@.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("@.status").isEqualTo("WAITING");
    }
}