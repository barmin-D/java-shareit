package ru.practicum.shareit.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto add(@RequestHeader("X-Sharer-User-Id") Integer userId,
                          @RequestBody BookingDto bookingDto) {
        return bookingService.add(userId, bookingDto);
    }

    @GetMapping("/{bookingId}")
    public Optional<BookingFullDto> get(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                        @PathVariable Integer bookingId) {
        return bookingService.get(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public BookingFullDto approveBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                         @PathVariable Integer bookingId,
                                         @RequestParam Boolean approved) {
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping
    public Collection<BookingFullDto> getBookings(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                  @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookings(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingFullDto> getBookingsOwner(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                       @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookingsOwner(userId, state);
    }
}
