package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserMapperTest {

    @Test
    void toUserDto() {
        User user = new User(1, "name", "email@email.com");
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setName("name");
        userDto.setEmail("email@email.com");
        Assertions.assertEquals(UserMapper.toUserDto(user), userDto);
    }

    @Test
    void toUser() {
        User user = new User(1, "name", "email@email.com");
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setName("name");
        userDto.setEmail("email@email.com");
        Assertions.assertEquals(UserMapper.toUser(userDto), user);
    }
}
