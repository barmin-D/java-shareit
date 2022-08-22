package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingBookerDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;


public interface BookingMapper {
    BookingDto toBookingDto(Booking booking);

    Booking toBooking(BookingDto bookingDto, Item item, User user);

    BookingFullDto toBookingFullDto(Booking booking);

    BookingBookerDto toBookingBookerDto(Booking booking);
}
