package ru.practicum.shareit.request;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class ItemRequestStorage {
    private static Set<ItemRequest> requests = new HashSet<>();
}