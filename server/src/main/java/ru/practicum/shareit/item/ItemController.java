package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@Validated
@RequestMapping("/items")
public class ItemController {

    private static final String XSHARERUSERID = "X-SHARE-USER-Id";
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> create(@RequestHeader(XSHARERUSERID) Long userId,
                                          @RequestBody ItemDto itemDto) {
        log.info("POST запрос на создание новой вещи: {} от пользователя c id: {}", itemDto, userId);
        return ResponseEntity.ok(itemService.create(userId, itemDto));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> update(@RequestHeader(XSHARERUSERID) Long userId,
                                          @RequestBody ItemDto itemDto,
                                          @PathVariable("itemId") Long itemId) {
        log.info("PATCH запрос на обновление вещи id: {} пользователя c id: {}", itemId, userId);
        return ResponseEntity.ok(itemService.update(userId, itemId, itemDto));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> get(@RequestHeader(XSHARERUSERID) Long userId,
                                       @PathVariable Long itemId) {
        log.info("GET запрос на получение вещи c id: {}", itemId);
        return ResponseEntity.ok(itemService.getItemById(userId, itemId));
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAll(@RequestHeader(XSHARERUSERID) Long userId,
                                                @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("GET запрос на получение всех вещей пользователя c id: {}", userId);
        return ResponseEntity.ok(itemService.getAll(userId, from, size));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchItems(@RequestHeader(XSHARERUSERID) Long userId,
                                                     @RequestParam(name = "text") String text,
                                                     @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                     @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("GET запрос на поиск всех вещей c текстом: {}", text);
        return ResponseEntity.ok(itemService.search(userId, text, from, size));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> createComment(@RequestHeader(XSHARERUSERID) Long userId,
                                                    @Validated @RequestBody CommentDto commentDto,
                                                    @PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.createComment(userId, commentDto, itemId));
    }

}