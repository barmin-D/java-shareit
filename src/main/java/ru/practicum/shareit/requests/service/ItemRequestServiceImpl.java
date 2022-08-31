package ru.practicum.shareit.requests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemDbRepository;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.mapper.ItemRequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserDbRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private UserDbRepository userRepository;
    private ItemRequestRepository itemRequestRepository;
    private ItemRequestMapper itemRequestMapper;
    private ItemDbRepository itemRepository;
    private ItemMapper itemMapper;

    @Autowired
    public ItemRequestServiceImpl(UserDbRepository userRepository, ItemRequestRepository itemRequestRepository,
                                  ItemRequestMapper itemRequestMapper, ItemDbRepository itemRepository,
                                  ItemMapper itemMapper) {
        this.userRepository = userRepository;
        this.itemRequestRepository = itemRequestRepository;
        this.itemRequestMapper = itemRequestMapper;
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemRequestDto add(Integer userId, ItemRequestDto itemRequestDto) {
        if (userRepository.findById(userId).isPresent()) {
            if (itemRequestDto.getDescription() == null || itemRequestDto.getDescription().length() == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            itemRequestDto.setRequestor(userRepository.findById(userId).get());
            ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
            return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest), new ArrayList<>());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Collection<ItemRequestDto> get(Integer userId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Collection<ItemRequest> list = itemRequestRepository.findAllByRequestor(userId);
        Collection<ItemRequestDto> itemRequestDtoCollection = new ArrayList<>();
        if (!list.isEmpty()) {
            for (ItemRequest i : list) {
                itemRequestDtoCollection.add(getById(userId, i.getId()));
            }
        }
        return itemRequestDtoCollection;
    }

    @Override
    public Collection<ItemRequestDto> getAll(Integer userId, Integer from, Integer size) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (from < 0 || size <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Page<ItemRequest> list =
                itemRequestRepository.findAllByRequestorIdDesc(userId, PageRequest.of(from / size, size));
        Collection<ItemRequestDto> itemRequestDtoCollection = new ArrayList<>();
        if (!list.isEmpty()) {
            for (ItemRequest i : list) {
                itemRequestDtoCollection.add(getById(userId, i.getId()));
            }
        }
        return itemRequestDtoCollection;
    }

    @Override
    public ItemRequestDto getById(Integer userId, Integer requestId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!itemRequestRepository.findById(requestId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).get();
        List<ItemDto> list = itemRepository.findAllByItemRequest(requestId).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
        return itemRequestMapper.toItemRequestDto(itemRequest, list);
    }
}
