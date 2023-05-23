package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;


import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap();
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
        checkById(id);
        return users.get(id);
    }

    public void removeUserById(int id) {
        checkById(id);
        users.remove(id);
    }

    @Override
    public Boolean checkEmail(User user) {
        return users.values()
                .stream()
                .anyMatch(u -> u.getEmail().contains(user.getEmail()) && !Objects.equals(u.getId(), user.getId()));
    }

    public void checkById(int id) {
        if (users.get(id) == null) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
    }

}