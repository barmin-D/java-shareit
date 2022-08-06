package ru.practicum.shareit.item.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private final Logger log = LoggerFactory.getLogger(ItemRepositoryImpl.class);
    private Integer idCounter = 1;
    private Map<Integer, Item> items = new HashMap<>();
    private ItemMapper itemMapper;
    private UserRepository userRepository;

    @Autowired
    public ItemRepositoryImpl(ItemMapper itemMapper, UserRepository userRepository) {
        this.itemMapper = itemMapper;
        this.userRepository = userRepository;
    }

    public Integer getIdCounter() {
        return idCounter++;
    }

    @Override
    public Item get(Integer itemId) {
        return items.get(itemId);
    }

    @Override
    public Item addNewItem(Item item) {
        validateItem(item);
        log.debug("Предмет сохранен");
        item.setId(getIdCounter());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public void deleteItem(Integer userId, Integer itemId) {
        if (items.containsKey(itemId)) {
            Item item = items.get(itemId);
            if (item.getOwner().equals(userRepository.get(userId))) {
                items.remove(itemId);
            } else {
                log.error("Попытка удаления предмета который не пренадлежит пользователю");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        } else {
            log.error("Попытка удаления предмета которого нет");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Collection<Item> getItems(Integer userId) {
        return items.values().stream().filter(item -> item.getOwner().getId() == userId).collect(Collectors.toList());
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Collection<Item> search(Integer userId, String text) {
        if (text.length() == 0) {
            return new ArrayList<>();
        }
        String textString = text.toUpperCase(Locale.ROOT);
        return items.values().stream()
                .filter(item -> (item.getName() + item.getDescription()).toUpperCase(Locale.ROOT).contains(textString))
                .filter(item -> item.getAvailable() == true)
                .collect(Collectors.toList());
    }

    private Item validateItem(Item item) {
        if (items.containsValue(item)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (item.getOwner() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (item.getName().length() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (item.getAvailable() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return item;
    }
}
