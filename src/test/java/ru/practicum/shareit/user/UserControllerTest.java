package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.exception.NullParamException;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;
    private List<User> users;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(userController).build();
        users = new LinkedList<>();
        User user = new User(1, "name", "a@email.ru");
        users.add(user);
    }

    @Test
    void show() throws Exception {
        when(userService.getUsers()).thenReturn(users);

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(users.get(0).getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(users.get(0).getName())))
                .andExpect(jsonPath("$[0].email", is(users.get(0).getEmail())));
    }

    @Test
    void create() throws Exception {
        when(userService.addUser(any())).thenReturn(users.get(0));

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(users.get(0)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(users.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.name", is(users.get(0).getName())))
                .andExpect(jsonPath("$.email", is(users.get(0).getEmail())));
    }

    @Test
    void put() throws Exception {
        when(userService.update(any())).thenReturn(users.get(0));

        mvc.perform(MockMvcRequestBuilders.put("/users")
                        .content(mapper.writeValueAsString(new UserDto(1, "name", "a@email.ru")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(users.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.name", is(users.get(0).getName())))
                .andExpect(jsonPath("$.email", is(users.get(0).getEmail())));
    }

    @Test
    void update() throws Exception {
        when(userService.update(any())).thenReturn(users.get(0));

        mvc.perform(MockMvcRequestBuilders.patch("/users/1")
                        .content(mapper.writeValueAsString(new UserDto(1, "name", "a@email.ru")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(users.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.name", is(users.get(0).getName())))
                .andExpect(jsonPath("$.email", is(users.get(0).getEmail())));
    }

    @Test
    void getById() throws Exception {
        when(userService.getUser(1)).thenReturn(java.util.Optional.ofNullable(users.get(0)));

        mvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(users.get(0).getId()), Long.class))
                .andExpect(jsonPath("$.name", is(users.get(0).getName())))
                .andExpect(jsonPath("$.email", is(users.get(0).getEmail())));
    }

    @Test
    void userCheckerNullParamException() {
        User user = new User();
        user.setId(1);
        Exception exception = assertThrows(NullParamException.class, () -> {
            userController.userChecker(user);
        });
        System.out.println(exception.getMessage());
        String expectedMessage = "NullParamException";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    void userCheckerWrongDataException() {
        User user = new User();
        user.setId(1);
        user.setName("name");
        Exception exception = assertThrows(NullParamException.class, () -> {
            userController.userChecker(user);
        });
        System.out.println(exception.getMessage());
        String expectedMessage = "NullParamException";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}