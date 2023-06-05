package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Integer, Item> items = new HashMap<>();
    private int idItem = 0;

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Item createItem(Item item) {
        idItem++;
        item.setId(idItem);
        items.put(idItem, item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public Item getItemById(int id) {
        checkById(id);
        return items.get(id);
    }

    @Override
    public void removeItemById(int id) {
        checkById(id);
        items.remove(id);
    }

    public void checkById(int id) {
        if (items.get(id) == null) {
            throw new ObjectNotFoundException("Вещь не найдена");
        }
    }
}