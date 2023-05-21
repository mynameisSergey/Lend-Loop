package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserServiceInterface {

    UserDto createUser(UserDto userDto);

    UserDto updateUser(int id, UserDto userDto);

    List<UserDto> findAll();

    UserDto getUserById(int id);

    void removeUserById(int id);

}
