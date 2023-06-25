package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"db.name=test"})
@SpringJUnitConfig({ShareItApp.class, ItemRequestServiceImpl.class})
public class ItemRequestServiceIntegrTest {

    private final EntityManager em;
    private final ItemServiceImpl itemService;
    private final UserServiceImpl userService;
    private final ItemRequestServiceImpl itemRequestService;

    @Test
    void createRequestTest() {
        User user = new User(1L, "user", "user@email.ru");
        UserDto userDto = new UserDto(1L, "user", "user@email.ru");
        userService.createUser(userDto);
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "description", LocalDateTime.now());
        itemRequestService.createRequest(itemRequestDto, userDto.getId());
        ItemDto itemDto = new ItemDto(1L, "item", "item test", true, 1L);
        itemService.createItem(itemDto, 1L);
        TypedQuery<ItemRequest> query = em.createQuery("Select i from ItemRequest i where i.id = : id", ItemRequest.class);
        ItemRequest itemRequest = query.setParameter("id", itemRequestDto.getId()).getSingleResult();
        assertThat(itemRequest.getDescription(), equalTo(itemRequestDto.getDescription()));
    }
}