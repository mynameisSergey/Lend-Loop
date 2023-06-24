package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private long id;

    public User(long userId) {
        this.id = userId;
    }
}