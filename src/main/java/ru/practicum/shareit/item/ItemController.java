package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceInterface;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemServiceInterface itemService;
    private static final String XSHARERUSERID = "X-Sharer-User-Id";

    @Autowired
    public ItemController(ItemServiceInterface itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<ItemDto> findAll(@RequestHeader(XSHARERUSERID) int userId) {
        return itemService.getItemsByUserId(userId);
    }

    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader(XSHARERUSERID) int userId) {
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, @PathVariable int itemId, @RequestHeader(XSHARERUSERID) int userId) {
        return itemService.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable int itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam(name = "text") String text) {
        return itemService.searchItems(text);
    }

    @DeleteMapping("/{itemId}")
    public void removeItem(@PathVariable int itemId) {
        itemService.removeItemById(itemId);
    }
}