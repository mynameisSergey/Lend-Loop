package ru.practicum.shareit.item;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    Item createItem(Item item);

    Item updateItem(Item item);

    Item getItemById(int id);

    List<Item> findAll();

    void removeItemById(int id);

}