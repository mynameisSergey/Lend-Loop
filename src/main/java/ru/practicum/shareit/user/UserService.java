package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    UserDto add(UserDto userDto);

    UserDto update(Long userId, UserDto userDto);

    List<UserDto> getAll();

    void delete(Long userId);

    UserDto getUserById(Long userId);
}