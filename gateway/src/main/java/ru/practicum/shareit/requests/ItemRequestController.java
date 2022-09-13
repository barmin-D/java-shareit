package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                      @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info("Create request");
        return itemRequestClient.add(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> get(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("Get request");
        return itemRequestClient.get(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get all request");
        return itemRequestClient.getAll(userId, from, size);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                          @PathVariable Integer requestId) {
        log.info("Get request by id{}", requestId);
        return itemRequestClient.getById(userId, requestId);
    }
}
