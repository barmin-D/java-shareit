package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {
    private ItemRepository itemRepository;
    private ItemMapper itemMapper;
    private UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<ItemDto> get(Integer itemId) {
        return Optional.of(itemMapper.toItemDto(itemRepository.get(itemId)));
    }

    @Override
    public ItemDto addNewItem(Integer userId, ItemDto itemDto) {
        if (userRepository.getUsers().containsKey(userId)) {
            itemDto.setOwner(userRepository.get(userId).get());
            Item item = itemMapper.toItem(itemDto);
            return itemMapper.toItemDto(itemRepository.addNewItem(item));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteItem(Integer userId, Integer itemId) {
        itemRepository.deleteItem(userId, itemId);
    }

    @Override
    public ItemDto update(Integer userId, Integer itemId, ItemDto itemDto) {
        Item item = itemRepository.get(itemId);
        if (item.getOwner().getId().equals(userId)) {
            itemMapper.updateItemFromItemDto(itemDto, item);
            return itemMapper.toItemDto(itemRepository.update(item));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Collection<ItemDto> getItems(Integer userId) {
        Collection<ItemDto> itemDtoCollection = new ArrayList<>();
        Collection<Item> itemCollection = new ArrayList<>((Collection) itemRepository.getItems(userId));
        for (Item item : itemCollection) {
            itemDtoCollection.add(itemMapper.toItemDto(item));
        }
        return itemDtoCollection;
    }

    @Override
    public Collection<ItemDto> search(Integer userId, String text) {
        Collection<ItemDto> itemDtoCollection = new ArrayList<>();
        Collection<Item> itemCollection = new ArrayList<>((Collection) itemRepository.search(userId, text));
        for (Item item : itemCollection) {
            itemDtoCollection.add(itemMapper.toItemDto(item));
        }
        return itemDtoCollection;
    }
}
