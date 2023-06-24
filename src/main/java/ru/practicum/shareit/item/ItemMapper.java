package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.WrongDataException;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.Objects;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        if (item.getRequest() == null) {
            return new ItemDto(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.isAvailable()
            );
        }
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getRequest().getId()
        );
    }

    public static Item toItem(ItemDto itemDto, User owner, ItemRequest itemRequest) {
        if (itemDto.isAvailable() == null)
            throw new WrongDataException("");
        if (Objects.nonNull(owner)) {
            if (itemDto.getRequestId() != 0) {
                if (itemDto.getId() == 0) {
                    return new Item(
                            itemDto.getName(),
                            itemDto.getDescription(),
                            itemDto.isAvailable(),
                            owner,
                            itemRequest
                    );
                } else return new Item(
                        itemDto.getId(),
                        itemDto.getName(),
                        itemDto.getDescription(),
                        itemDto.isAvailable(),
                        owner,
                        itemRequest
                );
            } else {
                if (itemDto.getId() == 0) {
                    return new Item(
                            itemDto.getName(),
                            itemDto.getDescription(),
                            itemDto.isAvailable(),
                            owner
                    );
                } else return new Item(
                        itemDto.getId(),
                        itemDto.getName(),
                        itemDto.getDescription(),
                        itemDto.isAvailable(),
                        owner
                );
            }
        }

        throw new NotFoundException("");
    }
}