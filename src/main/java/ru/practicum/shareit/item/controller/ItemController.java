package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/items")
public class ItemController {
    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{itemId}")
    public Optional<ItemDto> get(@PathVariable Integer itemId) {
        return itemService.get(itemId);
    }

    @PostMapping
    public ItemDto add(@RequestHeader("X-Later-User-Id") Integer userId,
                       @Valid @RequestBody ItemDto itemDto) {
        return itemService.addNewItem(userId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Later-User-Id") Integer userId,
                           @PathVariable Integer itemId) {
        itemService.deleteItem(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Later-User-Id") Integer userId,
                          @PathVariable Integer itemId, @Valid @RequestBody ItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping
    public Collection<ItemDto> getItems(@RequestHeader("X-Later-User-Id") Integer userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("search")
    public Collection<ItemDto> search(@RequestHeader("X-Later-User-Id") Integer userId,
                                      @RequestParam(required = false) String text) {
        return itemService.search(userId, text);
    }
}
