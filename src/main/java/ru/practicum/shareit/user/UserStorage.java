package ru.practicum.shareit.user;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserStorage {

    List<User> findAll();

    User createUser(User user);

    User updateUser(User user);

    User getUserById(int id);

    void removeUserById(int id);

    Boolean checkEmail(User user);
}