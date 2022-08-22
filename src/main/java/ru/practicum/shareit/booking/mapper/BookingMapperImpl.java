package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingBookerDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDto.BookingDtoBuilder;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Booking.BookingBuilder;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Component
public class BookingMapperImpl implements BookingMapper {

    @Override
    public BookingDto toBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        BookingDtoBuilder bookingDto = BookingDto.builder();
        bookingDto.id(booking.getId());
        bookingDto.start(booking.getStart());
        bookingDto.end(booking.getEnd());
        bookingDto.itemId(booking.getItem().getId());
        bookingDto.bookerId(booking.getBooker().getId());
        bookingDto.status(booking.getStatus());
        return bookingDto.build();
    }

    @Override
    public Booking toBooking(BookingDto BookingDto, Item item, User user) {
        if (BookingDto == null) {
            return null;
        }
        BookingBuilder booking = Booking.builder();
        booking.id(BookingDto.getId());
        booking.start(BookingDto.getStart());
        booking.end(BookingDto.getEnd());
        booking.item(item);
        booking.booker(user);
        booking.status(BookingDto.getStatus());
        return booking.build();
    }

    @Override
    public BookingFullDto toBookingFullDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        return BookingFullDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
    }

    @Override
    public BookingBookerDto toBookingBookerDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        return BookingBookerDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }
}
