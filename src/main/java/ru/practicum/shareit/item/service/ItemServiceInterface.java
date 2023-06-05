package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemServiceInterface {

    ItemDto createItem(ItemDto itemDto, int userId);

    ItemDto updateItem(ItemDto itemDto, int itemId, int userId);

    List<ItemDto> findAll();

    ItemDto getItemById(int id);

    List<ItemDto> searchItems(String text);

    void removeItemById(int id);

    List<ItemDto> getItemsByUserId(int userId);

}