package ru.practicum.shareit.user;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceImplTest {
    @Autowired
    private UserService userService;


    @Test
    @Order(1)
    void getUsers() {
        User user = new User(1, "Name", "qwerty@mail.ru");
        assertThat(userService.getUsers().toString(), equalTo("[]"));
        userService.addUser(user);
        assertThat(userService.getUsers().get(0), equalTo(user));
        userService.deleteUser(1);
    }


    @Test
    @Order(2)
    void getUser() {
        User user = new User(2, "Name", "qwerty@mail.ru");
        userService.addUser(user);
        assertThat(userService.getUser(2).get(), equalTo(user));
        userService.deleteUser(2);
    }

    @Test
    @Order(3)
    void isEmailExist() {
        assertThat(userService.isEmailExist("email@email.com"), equalTo(false));
        userService.addUser(new User(3, "name", "new@email.ru"));
        assertThat(userService.isEmailExist("new@email.ru"), equalTo(true));
        userService.deleteUser(3);
    }

    @Test
    @Order(4)
    void setUsers() {
        Set<User> users = new HashSet<>();
        User user = new User(4, "Name", "qwerty@mail.ru");
        User user1 = new User(5, "Name", "qwerty1@mail.ru");
        users.add(user);
        users.add(user1);
        userService.setUsers(users);
        assertThat(userService.getUsers().toString(), equalTo(users.toString()));
        userService.deleteUser(4);
        userService.deleteUser(5);
    }

    @Test
    @Order(5)
    void deleteUser() {
        User user = new User(6, "Name", "qwerty@mail.ru");
        userService.addUser(user);
        assertThat(userService.getUsers().get(0), equalTo(user));
        userService.deleteUser(6);
        assertThat(userService.getUsers().toString(), equalTo("[]"));
    }

    @Test
    @Order(6)
    void update() {
        UserDto userDto = new UserDto();
        userDto.setId(7);
        userDto.setEmail("email@email.ru");
        userDto.setName("userName");
        userService.update(userDto);
        assertThat(userService.getUsers().get(0), equalTo(UserMapper.toUser(userDto)));
        userDto = new UserDto();
        userDto.setId(7);
        userDto.setEmail("email@email.ru");
        userDto.setName("newUserName");
        userService.update(userDto);
        assertThat(userService.getUsers().get(0).getName(), equalTo(userDto.getName()));
        userService.deleteUser(7);
    }

}