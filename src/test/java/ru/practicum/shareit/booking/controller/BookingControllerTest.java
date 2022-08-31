package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.status.Status;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    @MockBean
    ItemService itemService;

    @MockBean
    BookingService bookingService;

    @Autowired
    private MockMvc mvc;
    private UserDto userDto;
    private ItemDto itemDto;
    private Item item;
    private User user;
    private CommentDto commentDto;
    private BookingFullDto bookingFullDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1, "testName", "test@email.ru");
        userService.saveUser(userDto);
        user = new User(1, "testName2", "test2@email.ru");
        itemDto = new ItemDto(null, "name", "description", true,
                null, null, null, null,
                null, new ArrayList<>());
        itemService.addNewItem(1, itemDto);
        item = new Item(1, "name", "test", true, user, null);
        commentDto = new CommentDto(null, "trololo", null, null);
        itemService.createComment(1, 1, commentDto);
        bookingFullDto = new BookingFullDto(1, LocalDateTime.now(), LocalDateTime.now(),
                item, user, Status.WAITING);
    }

    @Test
    void add() throws Exception {
        BookingDto bookingDto = new BookingDto(1, 1, 1, LocalDateTime.now(),
                LocalDateTime.now(), Status.WAITING);

        when(bookingService.add(1, bookingDto)).thenReturn(bookingDto);
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Integer.class))
                .andExpect(jsonPath("$.itemId", is(bookingDto.getId())))
                .andExpect(jsonPath("$.status", is("WAITING")));
    }

    @Test
    void get() throws Exception {
        add();
        when(bookingService.get(1, 1)).thenReturn(Optional.ofNullable(bookingFullDto));

        mvc.perform(MockMvcRequestBuilders.get("/bookings/1")
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingFullDto.getId())))
                .andExpect(jsonPath("$.item.id", is(bookingFullDto.getItem().getId())))
                .andExpect(jsonPath("$.status", is("WAITING")));
    }

    @Test
    void approveBooking() throws Exception {
        add();
        bookingFullDto.setStatus(Status.APPROVED);
        when(bookingService.approveBooking(1, 1, true)).thenReturn(bookingFullDto);

        mvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingFullDto.getId())))
                .andExpect(jsonPath("$.item.id", is(bookingFullDto.getItem().getId())))
                .andExpect(jsonPath("$.status", is("APPROVED")));
    }

    @Test
    void getBookingsAll() throws Exception {
        when(bookingService.getBookingsOwner(1, "ALL", 0, 10))
                .thenReturn(List.of(bookingFullDto));

        mvc.perform(MockMvcRequestBuilders.get("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getBookingsFuture() throws Exception {
        when(bookingService.getBookingsOwner(1, "FUTURE", 0, 10))
                .thenReturn(List.of(bookingFullDto));

        mvc.perform(MockMvcRequestBuilders.get("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getBookingsCurrent() throws Exception {
        when(bookingService.getBookingsOwner(1, "CURRENT", 0, 10))
                .thenReturn(List.of(bookingFullDto));

        mvc.perform(MockMvcRequestBuilders.get("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getBookingsPast() throws Exception {
        when(bookingService.getBookingsOwner(1, "PAST", 0, 10))
                .thenReturn(List.of(bookingFullDto));

        mvc.perform(MockMvcRequestBuilders.get("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getBookingsOwnerAll() throws Exception {
        when(bookingService.getBookingsOwner(1, "ALL", 0, 10))
                .thenReturn(List.of(bookingFullDto));

        mvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getBookingsOwnerCurrent() throws Exception {
        when(bookingService.getBookingsOwner(1, "CURRENT", 0, 10))
                .thenReturn(List.of(bookingFullDto));

        mvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getBookingsOwnerFuture() throws Exception {
        when(bookingService.getBookingsOwner(1, "FUTURE", 0, 10))
                .thenReturn(List.of(bookingFullDto));

        mvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getBookingsOwnerPast() throws Exception {
        when(bookingService.getBookingsOwner(1, "PAST", 0, 10))
                .thenReturn(List.of(bookingFullDto));

        mvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}