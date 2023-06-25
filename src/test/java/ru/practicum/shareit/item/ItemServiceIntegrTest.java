package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.request.ItemRequestDto;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
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
@SpringJUnitConfig({ShareItApp.class, ItemServiceImpl.class})
public class ItemServiceIntegrTest {

    private final EntityManager em;
    private final ItemServiceImpl itemService;
    private final UserServiceImpl userService;
    private final ItemRequestServiceImpl itemRequestService;

    @Test
    void createItemTest() {
        User user = new User(1L, "user", "user@email.ru");
        UserDto userDto = new UserDto(1L, "user", "user@email.ru");
        userService.createUser(userDto);
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "description", LocalDateTime.now());
        itemRequestService.createRequest(itemRequestDto, userDto.getId());
        ItemDto itemDto = new ItemDto(1L, "item", "item test", true, 1L);
        itemService.createItem(itemDto, 1L);
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = : id", Item.class);
        Item item = query.setParameter("id", itemDto.getId()).getSingleResult();
        assertThat(item.getName(), equalTo(itemDto.getName()));
        assertThat(item.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(item.getOwner().getId(), equalTo(userDto.getId()));
        assertThat(item.getOwner().getName(), equalTo(userDto.getName()));
        assertThat(item.getOwner().getEmail(), equalTo(userDto.getEmail()));
        assertThat(item.getRequest().getId(), equalTo(itemRequestDto.getId()));
        assertThat(item.getRequest().getDescription(), equalTo(itemRequestDto.getDescription()));
    }
}