package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.comment.CommentDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(ItemDto item, long ownerId);

    ItemDto updateItem(ItemDto itemDto, long ownerId, long itemId);

    ItemDto getItemDtoById(long itemId, long userId);

    List<ItemDto> searchItems(String text);

    List<ItemDto> searchItems(String text, int from, int size);

    List<ItemDto> show(long id);

    Page<ItemDto> show(long id, int from, int size);

    ItemDto getItemById(long itemId);

    CommentDto addComment(CommentDto commentDto, long itemId, long userId);

    CommentDto getCommentDto(long itemId);

    void removeItem(long id);
}