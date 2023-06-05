package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectExistsException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceInterface {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User newUser = UserMapper.toUser(new User(), userDto);
        if (!userStorage.checkEmail(newUser)) {
            User createdUser = userStorage.createUser(newUser);
            return UserMapper.toUserDto(createdUser);
        } else {
            throw new ObjectExistsException("Пользователь уже есть в системе");
        }
    }

    @Override
    public UserDto updateUser(int id, UserDto userDto) {
        User user = UserMapper.toUser(new User(userStorage.getUserById(id)), userDto);
        user.setId(id);
        if (!userStorage.checkEmail(user)) {
            User updatedUser = userStorage.updateUser(user);
            return UserMapper.toUserDto(updatedUser);
        } else {
            throw new ObjectExistsException("Пользователь уже есть в системе");
        }
    }

    @Override
    public List<UserDto> findAll() {
        return userStorage.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(int id) {
        return UserMapper.toUserDto(userStorage.getUserById(id));
    }

    @Override
    public void removeUserById(int id) {
        userStorage.getUserById(id);
        userStorage.removeUserById(id);
    }
}