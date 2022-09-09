package ru.practicum.shareit.requests.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRequestRepositoryTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    private User user;
    private PageRequest pageable;
    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        Integer from = 0;
        Integer size = 10;
        user = new User(null, "testName2", "test2@email.ru");
        em.persist(user);
        itemRequest = new ItemRequest(null, "test", user, LocalDateTime.now());
        em.persist(itemRequest);
        pageable = PageRequest.of(from, size);
    }

    @Test
    void findAllByRequestor() {
        Collection<ItemRequest> list = itemRequestRepository.findAllByRequestor(user.getId());
        assertEquals(list.size(), 1, "неверное количество бронирований");
        assertEquals(list.stream().collect(Collectors.toList()).get(0).getRequestor().getId(), user.getId(),
                "неверное количество бронирований");
    }

    @Test
    void findAllByRequestorIdDesc() {
        Page<ItemRequest> list = itemRequestRepository.findAllByRequestorIdDesc(user.getId(), pageable);
        assertEquals(list.stream().count(), 0, "неверное количество бронирований");
    }
}