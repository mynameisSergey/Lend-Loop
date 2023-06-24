package ru.practicum.shareit.booking;

import lombok.Data;

@Data
public class Item {
    private long id;
    private String name;
    private long ownerId;
}