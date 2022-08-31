package ru.practicum.shareit.requests.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.service.ItemRequestService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ItemRequestDto add(@RequestHeader("X-Sharer-User-Id") Integer userId,
                              @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.add(userId, itemRequestDto);
    }

    @GetMapping
    public Collection<ItemRequestDto> get(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemRequestService.get(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAll(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {
        return itemRequestService.getAll(userId, from, size);
    }

    @GetMapping("{requestId}")
    public ItemRequestDto getById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                  @PathVariable Integer requestId) {
        return itemRequestService.getById(userId, requestId);
    }
}
