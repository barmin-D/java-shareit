package ru.practicum.shareit.booking.service;

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
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Rollback(false)
@SpringBootTest(
        properties = "db.name=test3",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {
    private final EntityManager em;
    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;
    private UserDto userDto;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        userDto = makeUserDto(1, "user", "user@user.ru");
        userService.saveUser(userDto);
        userDto = makeUserDto(2, "userrr", "userrr@user.ru");
        userService.saveUser(userDto);
        itemDto = makeItemDto(1, "name", "description", true,
                new User(1, "torti", "torti@email"), null, null, null,
                null, new ArrayList<>());
        itemService.addNewItem(1, itemDto);
    }

    private <E> ItemDto makeItemDto(int i, String name, String description, boolean b, User torti, Object o,
                                    Object o1, Object o2, Object o3, ArrayList<E> es) {
        ItemDto dto = new ItemDto();
        dto.setId(i);
        dto.setName(name);
        dto.setDescription(description);
        dto.setAvailable(b);
        dto.setOwner(torti);
        dto.setRequestId((Integer) o);
        dto.setRequest((ItemRequest) o1);
        dto.setLastBooking((BookingBookerDto) o2);
        dto.setNextBooking((BookingBookerDto) o3);
        dto.setComments((List<CommentDto>) es);
        return dto;
    }

    private UserDto makeUserDto(Integer id, String name, String email) {
        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setName(name);
        dto.setEmail(email);
        return dto;
    }

    @Test
    void add() {
        BookingDto bookingDto = new BookingDto(1, 1, 1, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), Status.WAITING);
        bookingService.add(2, bookingDto);

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.id=:id", Booking.class);
        Booking booking = query.setParameter("id", bookingDto.getId())
                .getSingleResult();

        assertThat(booking.getId(), notNullValue());
        assertThat(booking.getItem().getId(), equalTo(bookingDto.getItemId()));
        assertThat(booking.getBooker().getId(), equalTo(2));
        assertThat(booking.getStatus(), equalTo(Status.WAITING));
    }

    @Test
    void get() {
        BookingDto bookingDto = new BookingDto(1, 1, 1, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), Status.WAITING);
        bookingService.add(2, bookingDto);

        Optional<BookingFullDto> dto = bookingService.get(1, 1);

        assertThat(dto.get().getId(), notNullValue());
    }

    @Test
    void approveBooking() {
        BookingDto bookingDto = new BookingDto(1, 1, 1, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), Status.WAITING);
        bookingService.add(2, bookingDto);

        bookingService.approveBooking(1, 1, true);

        Optional<BookingFullDto> dto = bookingService.get(1, 1);

        assertThat(dto.get().getId(), notNullValue());
        assertThat(dto.get().getStatus(), equalTo(Status.APPROVED));
    }

    @Test
    void getBookingsOwnerAll() {
        BookingDto bookingDto = new BookingDto(1, 1, 1, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), Status.WAITING);
        bookingService.add(2, bookingDto);

        bookingService.approveBooking(1, 1, true);

        Collection<BookingFullDto> list = bookingService.getBookingsOwner(1, "ALL", 1, 10);

        assertThat(list.size(), equalTo(1));
    }

    @Test
    void getBookingsOwnerFuture() {
        BookingDto bookingDto = new BookingDto(1, 1, 1, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), Status.WAITING);
        bookingService.add(2, bookingDto);

        bookingService.approveBooking(1, 1, true);

        Collection<BookingFullDto> list = bookingService.getBookingsOwner(2, "FUTURE", 1, 10);

        assertThat(list.size(), equalTo(1));
    }

    @Test
    void getBookingsOwnerPast() {
        BookingDto bookingDto = new BookingDto(1, 1, 1, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), Status.WAITING);
        bookingService.add(2, bookingDto);

        bookingService.approveBooking(1, 1, true);

        Collection<BookingFullDto> list = bookingService.getBookingsOwner(1, "PAST", 1, 10);

        assertThat(list.size(), equalTo(0));
    }

    @Test
    void getBookingsOwnerCurrent() {
        BookingDto bookingDto = new BookingDto(1, 1, 1, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), Status.WAITING);
        bookingService.add(2, bookingDto);

        bookingService.approveBooking(1, 1, true);

        Collection<BookingFullDto> list = bookingService.getBookingsOwner(1, "CURRENT", 1, 10);

        assertThat(list.size(), equalTo(0));
    }

    @Test
    void getBookingsAll() {
        BookingDto bookingDto = new BookingDto(1, 1, 1, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), Status.WAITING);
        bookingService.add(2, bookingDto);

        bookingService.approveBooking(1, 1, true);

        Collection<BookingFullDto> list = bookingService.getBookingsOwner(1, "ALL", 1, 10);

        assertThat(list.size(), equalTo(1));
    }

    @Test
    void getBookingsFuture() {
        BookingDto bookingDto = new BookingDto(1, 1, 1, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), Status.WAITING);
        bookingService.add(2, bookingDto);

        bookingService.approveBooking(1, 1, true);

        Collection<BookingFullDto> list = bookingService.getBookingsOwner(1, "FUTURE", 1, 10);

        assertThat(list.size(), equalTo(1));
    }

    @Test
    void getBookingsCurrent() {
        BookingDto bookingDto = new BookingDto(1, 1, 1, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), Status.WAITING);
        bookingService.add(2, bookingDto);

        bookingService.approveBooking(1, 1, true);

        Collection<BookingFullDto> list = bookingService.getBookingsOwner(1, "CURRENT", 1, 10);

        assertThat(list.size(), equalTo(0));
    }

    @Test
    void getBookingsPast() {
        BookingDto bookingDto = new BookingDto(1, 1, 1, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), Status.WAITING);
        bookingService.add(2, bookingDto);

        bookingService.approveBooking(1, 1, true);

        Collection<BookingFullDto> list = bookingService.getBookingsOwner(1, "PAST", 1, 10);

        assertThat(list.size(), equalTo(0));
    }
}