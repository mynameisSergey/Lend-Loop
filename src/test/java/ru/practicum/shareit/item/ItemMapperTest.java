package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.transaction.Transactional;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"db.name=item_test"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemMapperTest {

    @Test
    @Order(1)
    void toItemDto() {
        var item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setDescription("d");
        var user = new User();
        user.setId(2L);
        user.setName("Test");
        var request = new ItemRequest();
        var original = new Item();
        original.setId(1L);
        original.setName("name");
        original.setRequest(request);
        original.setOwner(user);
        original.setDescription("d");
        var result = ItemMapper.toItemDto(item);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(original.getId(), result.getId());
        Assertions.assertEquals(original.getName(), result.getName());
        Assertions.assertEquals(original.getDescription(), result.getDescription());
    }

    @Test
    @Order(2)
    void toItem() {
        var item = new ItemDto();
        item.setId(1L);
        item.setName("name");
        item.setAvailable(true);
        var user = new User();
        user.setId(2L);
        user.setName("name");
        var request = new ItemRequest();
        var original = new Item();
        original.setId(1L);
        original.setName("name");
        original.setRequest(request);
        original.setOwner(user);
        var result = ItemMapper.toItem(item, user, request);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(original.getId(), result.getId());
        Assertions.assertEquals(original.getName(), result.getName());
        Assertions.assertEquals(original.getDescription(), result.getDescription());
    }
}