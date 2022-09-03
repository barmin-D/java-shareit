package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private BookingRepository bookingRepository;
    private User user;
    private Item item;
    private Booking booking;
    private PageRequest pageable;

    @BeforeEach
    void setUp() {
        Integer from = 0;
        Integer size = 10;
        user = new User(null, "testName2", "test2@email.ru");
        em.persist(user);
        item = new Item(null, "name", "test", true, user, null);
        em.persist(item);
        booking = new Booking(null, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), item, user,
                Status.WAITING);
        em.persist(booking);
        pageable = PageRequest.of(from, size);
    }

    @Test
    void findAllByBookerCurrent() {
        List<Booking> list = bookingRepository.findAllByBookerCurrent(user, LocalDateTime.now(),
                LocalDateTime.now(), pageable);
        assertEquals(list.size(), 0, "неверное количество бронирований");
    }

    @Test
    void findAllByOwnerCurrent() {
        List<Booking> list = bookingRepository.findAllByOwnerCurrent(user, LocalDateTime.now(),
                LocalDateTime.now(), pageable);
        assertEquals(list.size(), 0, "неверное количество бронирований");
    }

    @Test
    void findAllByBookerInFuture() {
        List<Booking> list = bookingRepository.findAllByBookerInFuture(user, pageable);
        assertEquals(list.size(), 1, "неверное количество бронирований");
        assertEquals(list.get(0).getBooker(), user);
        assertEquals(list.get(0).getItem(), item);
    }

    @Test
    void findAllByBookerInPast() {
        List<Booking> list = bookingRepository.findAllByBookerInPast(user, LocalDateTime.now(), pageable);
        assertEquals(list.size(), 0, "неверное количество бронирований");
    }

    @Test
    void findFirstByItemOwnerAsc() {
        booking = bookingRepository.findFirstByItemOwnerAsc(item.getId(), user, LocalDateTime.now());
        assertNotNull(booking);
    }

    @Test
    void findFirstByItemOwnerDesc() {
        booking = bookingRepository.findFirstByItemOwnerDesc(item.getId(), user, LocalDateTime.now());
        assertNull(booking);
    }

    @Test
    void findFirstByItemDesc() {
        booking = bookingRepository.findFirstByItemDesc(item, LocalDateTime.now(), Status.APPROVED);
        assertNull(booking);
    }

    @Test
    void findFirstByItemAsc() {
        booking = bookingRepository.findFirstByItemAsc(item, LocalDateTime.now(), Status.APPROVED);
        assertNull(booking);
    }

    @Test
    void findFirstByItemAndBookerDesc() {
        booking = bookingRepository.findFirstByItemAndBookerDesc(item, user, LocalDateTime.now(), Status.WAITING);
        assertNull(booking);
    }
}