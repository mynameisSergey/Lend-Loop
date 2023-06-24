package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.ObjectNotFoundException;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = "db.name=test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {

    private final UserService userService;
    private UserDto userDto;
    private UserDto user;

    @BeforeEach
    void beforeEach() {
        userDto = new UserDto(1L, "user", "user@email.ru");
        user = userService.createUser(userDto);
    }

    @Order(1)
    @Test
    void createUserTest() {
        assertThat(user.getId(), equalTo(userDto.getId()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
        assertThat(user.getName(), equalTo(userDto.getName()));
    }

    @Order(2)
    @Test
    void updateUserTest() {
        UserDto userDto1 = new UserDto(1L, "userUpdate", "userUpdate@email.ru");
        UserDto user1 = userService.updateUser(1L, userDto1);
        assertThat(userDto1.getId(), equalTo(user1.getId()));
        assertThat(userDto1.getEmail(), equalTo(user1.getEmail()));
        assertThat(userDto1.getName(), equalTo(user1.getName()));
        UserDto userDto2 = new UserDto(7L, "userUpdate", "userUpdate@email.ru");
        assertThrows(ObjectNotFoundException.class, () -> userService.updateUser(7L, userDto2));
    }

    @Order(3)
    @Test
    void getUserByIdTest() {
        UserDto user1 = userService.getUserById(1L);
        assertThat(user1.getId(), equalTo(user.getId()));
        assertThat(user1.getEmail(), equalTo(user.getEmail()));
        assertThat(user1.getName(), equalTo(user.getName()));
    }

    @Order(4)
    @Test
    void getUserByIdNotExistsTest() {
        Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.getUserById(6L));
    }

    @Order(5)
    @Test
    void findAllTest() {
        List<UserDto> users = userService.findAll();
        assertThat(users.size(), equalTo(1));
    }

    @Order(6)
    @Test
    void removeUserByIdTest() {
        userDto = new UserDto(2L, "user", "user@gmail.ru");
        user = userService.createUser(userDto);
        userService.removeUserById(2L);
        List<UserDto> users = userService.findAll();
        assertThat(users.size(), equalTo(1));
    }

    @Order(7)
    @Test
    void removeUserByIdFailIdTest() {
        Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.removeUserById(7L));
    }
}