package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> get(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                      @PathVariable Integer itemId) {
        log.info("Get item");
        return itemClient.get(userId, itemId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                      @Valid @RequestBody ItemDto itemDto) {
        log.info("Create item");
        return itemClient.addNewItem(userId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                             @PathVariable Integer itemId) {
        log.info("Delete item");
        return itemClient.deleteItem(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                         @PathVariable Integer itemId, @RequestBody ItemDto itemDto) {
        log.info("Patch item");
        return itemClient.update(userId, itemId, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                           @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                           Integer from,
                                           @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get items");
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("search")
    public ResponseEntity<Object> search(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                         @RequestParam(required = false) String text) {
        log.info("Search items");
        return itemClient.search(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                @PathVariable Integer itemId,
                                                @RequestBody @Valid CommentDto commentDto) {
        log.info("Create comment");
        return itemClient.createComment(userId, itemId, commentDto);
    }
}
