package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService implements ItemServiceInterface {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto createItem(ItemDto itemDto, int userId) {
        Item newItem = ItemMapper.toItem(new Item(), itemDto);
        newItem.setOwner(userStorage.getUserById(userId));
        Item createdItem = itemStorage.createItem(newItem);
        return ItemMapper.toItemDto(createdItem);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, int itemId, int userId) {
        Item item = ItemMapper.toItem(new Item(itemStorage.getItemById(itemId)), itemDto);
        if (!Objects.equals(item.getOwner().getId(), userId)) {
            throw new ObjectNotFoundException("User is not owner of item!");
        }
        Item updatedItem = itemStorage.updateItem(item);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public List<ItemDto> findAll() {
        return itemStorage.findAll()
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(int id) {
        return ItemMapper.toItemDto(itemStorage.getItemById(id));
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (StringUtils.isBlank(text)) {
            return new ArrayList<>();
        }
        return itemStorage.findAll()
                .stream()
                .filter(i -> StringUtils.containsIgnoreCase(i.getDescription(), text) && i.getAvailable())
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public void removeItemById(int id) {
        itemStorage.getItemById(id);
        itemStorage.removeItemById(id);
    }

    @Override
    public List<ItemDto> getItemsByUserId(int userId) {
        return itemStorage.findAll()
                .stream()
                .filter(i -> Objects.equals(i.getOwner().getId(), userId))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}