package ru.practicum.shareit.user;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItApp;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"db.name=test"})
@SpringJUnitConfig({ShareItApp.class, UserServiceImpl.class})
public class UserServiceIntegrTest {

    private final EntityManager em;
    private final UserService userService;

    @Test
    void createUserTest() {
        UserDto userDto = new UserDto(1L, "user", "user@email.ru");
        userService.createUser(userDto);
        TypedQuery<User> query = em.createQuery("Select i from User i where i.id = : id", User.class);
        User user = query.setParameter("id", userDto.getId()).getSingleResult();
        assertThat(user.getId(), equalTo(userDto.getId()));
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }
}
