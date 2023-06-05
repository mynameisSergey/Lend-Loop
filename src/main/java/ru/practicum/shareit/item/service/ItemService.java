package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto, Long userId);

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId);

    List<ItemDto> getItemsByUserId(Long userId);

    ItemDto getItemById(Long id, Long userId);

    void removeItemById(Long id);

    List<ItemDto> searchItems(String text);

    CommentDto createComment(CommentDto commentDto, Long userId, Long itemId);
}