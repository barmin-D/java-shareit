package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemDbRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemDbRepositoryTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private ItemDbRepository itemDbRepository;
    private User user;
    private Item item;

    @BeforeEach
    void setUp() {
        user = new User(null, "testName2", "test2@email.ru");
        em.persist(user);
        item = new Item(null, "name", "test", true, user, null);
        em.persist(item);
    }

    @Test
    void search() {
        List<Item> list = itemDbRepository.search("nam");
        assertEquals(list.size(), 1, "неверное количество предметов");
    }

    @Test
    void findAllByItemRequest() {
        List<Item> list = itemDbRepository.findAllByItemRequest(1);
        assertEquals(list.size(), 0, "неверное количество предметов");
    }
}