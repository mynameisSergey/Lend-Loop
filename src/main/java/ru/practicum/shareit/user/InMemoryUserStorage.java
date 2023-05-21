package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap();
    private int id = 0;

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        id++;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return users.get(user.getId());
        } else {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
    }

    public User getUserById(int id) {
        if (users.get(id) != null) {
            return users.get(id);
        } else {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
    }

    public void removeUserById(int id) {
        if (users.get(id) != null) {
            users.remove(id);
        } else {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public Boolean checkEmail(User user) {
        return users.values()
                .stream()
                .anyMatch(u -> u.getEmail().contains(user.getEmail()) && !Objects.equals(u.getId(), user.getId()));
    }
}