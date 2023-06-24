package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    ItemResponseDto createItem(ItemDto itemDto, Long userId);

    ItemResponseDto updateItem(ItemDto itemDto, Long itemId, Long userId);

    List<ItemResponseDto> getItemsByUserId(Long userId, Integer from, Integer size);

    ItemResponseDto getItemById(Long id, Long userId);

    void removeItemById(Long id);

    List<ItemResponseDto> searchItems(String text, Integer from, Integer size);

    CommentResponseDto createComment(CommentDto commentDto, Long userId, Long itemId);
}