package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ItemStorage {
    private static Set<Item> items = new HashSet<>();
    private static int maxId = 0;

    public static int getMaxId() {
        maxId++;
        return maxId;
    }

    public static Set<Item> getItems() {
        return items;
    }

    public static Item getItem(int itemId) {
        return getItems().stream()
                .filter(item -> item.getId() == itemId)
                .findAny()
                .orElse(null);
    }

    private static void setItems(Set<Item> items) {
        ItemStorage.items = items;
    }

    public static Item addItem(Item item) {
        getItems().add(item);
        return item;
    }

    public static void deleteItem(int itemId) {
        getItems().removeIf(item -> item.getId() == itemId);
    }

    public static Item update(Item item) {
        Item updatedItem = getItem((int) item.getId());
        if (item.getName() != null) {
            if (!item.getName().equals("")) {
                updatedItem.setName(item.getName());
            }
        }
        if (item.getDescription() != null) {
            updatedItem.setDescription(item.getDescription());
        }
        if (item.getRequest() != null) {
            updatedItem.setRequest(item.getRequest());
        }
        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }
        deleteItem((int) item.getId());
        addItem(updatedItem);
        return updatedItem;
    }
}