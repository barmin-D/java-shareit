package ru.practicum.shareit.booking.mapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.booking.dto.BookingBookerDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Rollback(false)
@SpringBootTest(
        properties = "db.name=test7",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingMapperImplTest {
    @Autowired
    private BookingMapper bookingMapper;
    private Item item;
    private User user;
    private ItemDto itemDto;
    private CommentDto commentDto;
    private BookingDto bookingDto;
    private BookingFullDto bookingFullDto;
    private Booking booking;

    @BeforeEach
    void setUp() {
        user = new User(1, "testName2", "test2@email.ru");
        itemDto = new ItemDto(null, "name", "description", true,
                null, null, null, null,
                null, new ArrayList<>());
        item = new Item(1, "name", "test", true, user, null);
        commentDto = new CommentDto(null, "trololo", null, null);
        bookingDto = new BookingDto(1, 1, 1, LocalDateTime.now(),
                LocalDateTime.now(), Status.WAITING);
        bookingFullDto = new BookingFullDto(1, LocalDateTime.now(), LocalDateTime.now(),
                item, user, Status.WAITING);
        booking = new Booking(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1), item, user, Status.WAITING);
    }

    @Test
    void toBookingDto() {
        bookingDto = bookingMapper.toBookingDto(booking);
        assertEquals(bookingDto.getItemId(), booking.getItem().getId());
        assertEquals(bookingDto.getBookerId(), booking.getBooker().getId());
    }

    @Test
    void toBooking() {
        booking = bookingMapper.toBooking(bookingDto, item, user);
        assertEquals(bookingDto.getItemId(), booking.getItem().getId());
        assertEquals(bookingDto.getBookerId(), booking.getBooker().getId());
    }

    @Test
    void toBookingFullDto() {
        bookingFullDto = bookingMapper.toBookingFullDto(booking);
        assertEquals(bookingFullDto.getItem(), booking.getItem());
        assertEquals(bookingFullDto.getBooker(), booking.getBooker());
    }

    @Test
    void toBookingBookerDto() {
        BookingBookerDto bookingBookerDto = bookingMapper.toBookingBookerDto(booking);
        assertEquals(bookingBookerDto.getBookerId(), booking.getBooker().getId());
    }
}