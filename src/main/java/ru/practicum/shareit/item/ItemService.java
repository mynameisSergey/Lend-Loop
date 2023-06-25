package ru.practicum.shareit.item;

import org.hibernate.ObjectNotFoundException;

import java.util.*;

public interface ItemService {

    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    ItemDto getItemById(Long userId, Long itemId) throws ObjectNotFoundException;

    List<ItemDto> getAll(Long userId, Integer  from, Integer size);

    List<ItemDto> search(Long userId, String text, Integer  from, Integer size);

    CommentDto createComment(Long userId, CommentDto commentDto, Long itemId);
}