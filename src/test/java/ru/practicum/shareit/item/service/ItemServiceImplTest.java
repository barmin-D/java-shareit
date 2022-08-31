package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.booking.dto.BookingBookerDto;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Rollback(false)
@SpringBootTest(
        properties = "db.name=test2",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplTest {
    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;
    private ItemDto itemDto;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = makeUserDto(1, "user", "user@user.ru");
        userService.saveUser(userDto);
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
    void addNewItem() {
        itemDto = makeItemDto(1, "name", "description", true,
                new User(1, "torti", "torti@email"), null, null, null,
                null, new ArrayList<>());
        itemService.addNewItem(1, itemDto);

        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id=:id", Item.class);
        Item item = query.setParameter("id", itemDto.getId())
                .getSingleResult();

        assertThat(item.getId(), notNullValue());
        assertThat(item.getName(), equalTo(itemDto.getName()));
        assertThat(item.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(item.getOwner(), equalTo(itemDto.getOwner()));
    }

    @Test
    void get() {
        itemDto = makeItemDto(1, "name", "description", true,
                new User(1, "torti", "torti@email"), null, null, null,
                null, new ArrayList<>());
        itemService.addNewItem(1, itemDto);

        Optional<ItemDto> optionalItemDto = itemService.get(1, 1);

        assertThat(optionalItemDto.get().getId(), notNullValue());
        assertThat(optionalItemDto.get().getName(), equalTo(itemDto.getName()));
        assertThat(optionalItemDto.get().getDescription(), equalTo(itemDto.getDescription()));
        assertThat(optionalItemDto.get().getOwner(), equalTo(null));
    }

    @Test
    void deleteItem() {
        itemDto = makeItemDto(1, "name", "description", true,
                new User(1, "torti", "torti@email"), null, null, null,
                null, new ArrayList<>());
        itemService.addNewItem(1, itemDto);
        itemService.deleteItem(1, 1);

        Collection<ItemDto> list = itemService.getItems(1);

        assertThat(list, hasSize(2));
    }

    @Test
    void update() {
        itemDto = makeItemDto(1, "name", "description", true,
                new User(1, "torti", "torti@email"), null, null, null,
                null, new ArrayList<>());
        itemService.addNewItem(1, itemDto);
        itemDto = makeItemDto(1, "tortiss", null, true, null, null, null,
                null, null, null);
        itemService.update(1, 1, itemDto);

        Optional<ItemDto> optionalItemDto = itemService.get(1, 1);

        assertThat(optionalItemDto.get().getId(), notNullValue());
        assertThat(optionalItemDto.get().getName(), equalTo(itemDto.getName()));

    }

    @Test
    void getItems() {
        List<ItemDto> source = List.of(
                makeItemDto(1, "name", "description", true,
                        new User(1, "torti", "torti@email"), null, null, null,
                        null, new ArrayList<>()),
                makeItemDto(2, "name", "description", true,
                        new User(1, "torti", "torti@email"), null, null, null,
                        null, new ArrayList<>()),
                makeItemDto(3, "name", "description", true,
                        new User(1, "torti", "torti@email"), null, null, null,
                        null, new ArrayList<>()));
        for (ItemDto i : source) {
            itemService.addNewItem(1, i);
        }
        Collection<ItemDto> list = itemService.getItems(1);
        assertThat(list, hasSize(3));
        for (ItemDto s : source) {
            assertThat(list, hasItem(allOf(hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(s.getName())),
                    hasProperty("description", equalTo(s.getDescription()))
            )));
        }
    }

    @Test
    void search() {
        List<ItemDto> source = List.of(
                makeItemDto(4, "name", "description", true,
                        new User(1, "torti", "torti@email"), null, null, null,
                        null, new ArrayList<>()),
                makeItemDto(5, "name", "description", true,
                        new User(1, "torti", "torti@email"), null, null, null,
                        null, new ArrayList<>()),
                makeItemDto(6, "name", "description", true,
                        new User(1, "torti", "torti@email"), null, null, null,
                        null, new ArrayList<>()));
        for (ItemDto i : source) {
            itemService.addNewItem(1, i);
        }

        Collection<ItemDto> list = itemService.search(1, "desc");

        assertThat(list, hasSize(3));
    }
}