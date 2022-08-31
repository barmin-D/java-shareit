package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;

import java.util.Collection;
import java.util.Optional;

@Service
public interface BookingService {
    BookingDto add(Integer userId, BookingDto bookingDto);

    Optional<BookingFullDto> get(Integer userId, Integer bookingId);

    BookingFullDto approveBooking(Integer userId, Integer bookingId, Boolean approved);

    Collection<BookingFullDto> getBookingsOwner(Integer userId, String state, Integer from, Integer size);

    Collection<BookingFullDto> getBookings(Integer userId, String state, Integer from, Integer size);
}
