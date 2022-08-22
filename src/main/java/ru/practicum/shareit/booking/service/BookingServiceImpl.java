package ru.practicum.shareit.booking.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemDbRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDbRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;


@Service
public class BookingServiceImpl implements BookingService {
    private final LocalDateTime TIME = LocalDateTime.now();
    private ItemDbRepository itemRepository;
    private UserDbRepository userRepository;
    private BookingMapper bookingMapper;
    private BookingRepository bookingRepository;

    @Autowired
    public BookingServiceImpl(ItemDbRepository itemRepository, UserDbRepository userRepository,
                              BookingMapper bookingMapper, BookingRepository bookingRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingMapper = bookingMapper;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public BookingDto add(Integer userId, BookingDto bookingDto) {
        if (!bookingDto.getStart().isAfter(LocalDateTime.now())) {
            throw new InvalidException();
        }
        if (!bookingDto.getEnd().isAfter(LocalDateTime.now())) {
            throw new InvalidException();
        }
        if (userRepository.findById(userId).isPresent()) {
            User user = userRepository.findById(userId).get();
            if (itemRepository.findById(bookingDto.getItemId()).isPresent()) {
                Item item = itemRepository.findById(bookingDto.getItemId()).get();
                if (item.getOwner().getId().equals(userId)) {
                    throw new UserNotFoundException();
                }
                if (item.getAvailable()) {
                    Booking booking = bookingMapper.toBooking(bookingDto, item, user);
                    booking.setBooker(user);
                    booking.setStatus(Status.WAITING);
                    return bookingMapper.toBookingDto(bookingRepository.save(booking));
                } else {
                    throw new InvalidException();
                }
            } else {
                throw new ItemNotFoundException();
            }
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public Optional<BookingFullDto> get(Integer userId, Integer bookingId) {
        if (bookingRepository.findById(bookingId).isPresent()) {
            Booking booking = bookingRepository.findById(bookingId).get();
            if (booking.getBooker().getId().equals(userId) || booking.getItem().getOwner().getId().equals(userId)) {
                return Optional.of(bookingMapper.toBookingFullDto(booking));
            } else {
                throw new UserNotFoundException();
            }
        } else {
            throw new BookingNotFoundException();
        }
    }

    @Override
    public BookingFullDto approveBooking(Integer userId, Integer bookingId, Boolean approved) {
        if (bookingRepository.findById(bookingId).isPresent()) {
            if (userRepository.findById(userId).isPresent()) {
                Booking booking = bookingRepository.findById(bookingId).get();
                if (booking.getStatus().equals(Status.APPROVED)) {
                    throw new InvalidException();
                }
                if (booking.getItem().getOwner().getId().equals(userId)) {
                    if (approved) {
                        booking.setStatus(Status.APPROVED);
                    } else {
                        booking.setStatus(Status.REJECTED);
                    }
                    bookingRepository.save(booking);
                    return bookingMapper.toBookingFullDto(booking);
                } else {
                    throw new UserNotFoundException();
                }
            } else {
                throw new UserNotFoundException();
            }
        } else {
            throw new ItemNotFoundException();
        }
    }

    @SneakyThrows
    @Override
    public Collection<BookingFullDto> getBookingsOwner(Integer userId, String state) {
        if (userRepository.findById(userId).isPresent()) {
            User user = userRepository.findById(userId).get();
            Collection<BookingFullDto> list = new ArrayList<>();
            Collection<Booking> bookingCollection;
            switch (state) {
                case "ALL":
                    bookingCollection = bookingRepository.findAllByItemOwnerOrderByIdDesc(user);
                    break;
                case "CURRENT":
                    bookingCollection =
                            bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(user,
                                    LocalDateTime.now(), LocalDateTime.now());
                    break;
                case "PAST":
                    bookingCollection = bookingRepository.findAllByItemOwnerAndEndBeforeOrderByStartDesc(user,
                            LocalDateTime.now());
                    break;
                case "FUTURE":
                    bookingCollection = bookingRepository.findAllByBookerInFuture(user);
                    break;
                case "WAITING":
                    bookingCollection = bookingRepository.findAllByItemOwnerAndStatus(user, Status.WAITING);
                    break;
                case "REJECTED":
                    bookingCollection = bookingRepository.findAllByItemOwnerAndStatus(user, Status.REJECTED);
                    break;
                default:
                    throw new ValidationException(String.format("Unknown state: %s", state));
            }
            for (Booking b : bookingCollection) {
                list.add(bookingMapper.toBookingFullDto(b));
            }
            return list;
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public Collection<BookingFullDto> getBookings(Integer userId, String state) {
        if (userRepository.findById(userId).isPresent()) {
            User user = userRepository.findById(userId).get();
            Collection<BookingFullDto> list = new ArrayList<>();
            Collection<Booking> bookingCollection;
            switch (state) {
                case "ALL":
                    bookingCollection = bookingRepository.findAllByBookerOrderByStartDesc(user);
                    break;
                case "CURRENT":
                    bookingCollection = bookingRepository.findAllByBookerCurrent(user, LocalDateTime.now(),
                            LocalDateTime.now());
                    break;
                case "PAST":
                    bookingCollection = bookingRepository.findAllByBookerInPast(user, LocalDateTime.now());
                    break;
                case "FUTURE":
                    bookingCollection = bookingRepository.findAllByBookerInFuture(user);
                    break;
                case "WAITING":
                    bookingCollection = bookingRepository.findAllByBookerAndStatusOrderByStartAsc(user, Status.WAITING);
                    break;
                case "REJECTED":
                    bookingCollection = bookingRepository.findAllByBookerAndStatusOrderByStartAsc(user, Status.REJECTED);
                    break;
                default:
                    throw new ValidationException(String.format("Unknown state: %s", state));
            }
            for (Booking b : bookingCollection) {
                list.add(bookingMapper.toBookingFullDto(b));
            }
            return list;
        } else {
            throw new UserNotFoundException();
        }
    }
}