package ru.practicum.shareit.requests.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.booking.dto.BookingBookerDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Rollback(false)
@SpringBootTest(
        properties = "db.name=test4",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplTest {
    private final EntityManager em;
    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;
    private final ItemRequestService itemRequestService;
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

    private UserDto makeUserDto(Integer id, String name, String email) {
        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setName(name);
        dto.setEmail(email);
        return dto;
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

    @Test
    void add() {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1, "test", new ArrayList<ItemDto>(),
                new User(1, "name", "name@email.ru"), LocalDateTime.now());
        itemRequestService.add(2, itemRequestDto);

        TypedQuery<ItemRequest> query = em.createQuery("Select ir from ItemRequest ir where ir.id=:id",
                ItemRequest.class);
        ItemRequest itemRequest = query.setParameter("id", itemRequestDto.getId())
                .getSingleResult();

        assertThat(itemRequest.getId(), notNullValue());
        assertThat(itemRequest.getDescription(), equalTo(itemRequestDto.getDescription()));
        assertThat(itemRequest.getRequestor().getId(), equalTo(userDto.getId()));
        assertThat(itemRequest.getCreated(), equalTo(itemRequestDto.getCreated().withNano(0)));
    }

    @Test
    void get() {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1, "test", new ArrayList<ItemDto>(),
                new User(1, "name", "name@email.ru"), LocalDateTime.now());
        itemRequestService.add(2, itemRequestDto);

        Collection<ItemRequestDto> dto = itemRequestService.get(2);

        assertThat(dto.size(), equalTo(1));
    }

    @Test
    void getAll() {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1, "test", new ArrayList<ItemDto>(),
                new User(1, "name", "name@email.ru"), LocalDateTime.now());
        itemRequestService.add(2, itemRequestDto);

        Collection<ItemRequestDto> dto = itemRequestService.getAll(1, 0, 10);

        assertThat(dto.size(), equalTo(1));
    }

    @Test
    void getById() {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1, "test", new ArrayList<ItemDto>(),
                new User(1, "name", "name@email.ru"), LocalDateTime.now());
        itemRequestService.add(2, itemRequestDto);
        ItemRequestDto dto = itemRequestService.getById(2, 1);

        assertThat(dto.getId(), notNullValue());
        assertThat(dto.getDescription(), equalTo(itemRequestDto.getDescription()));
        assertThat(dto.getCreated(), equalTo(itemRequestDto.getCreated().withNano(0)));
    }
}