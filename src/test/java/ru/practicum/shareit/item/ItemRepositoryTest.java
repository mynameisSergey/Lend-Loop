package ru.practicum.shareit.item;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = {"db.name=item_test"})
public class ItemRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository repository;


    @Test
    @Order(1)
    void searchWithParams() {
        User user = new User(1, "Name", "qwerty@mail.ru");
        userRepository.save(user);
        Item item = new Item(1, "itemName", "item description", true, user);
        Assertions.assertEquals(repository.searchWithParams("item").toString(), "[]");
        repository.save(item);
        Assertions.assertNotNull(repository.searchWithParams("item"));
    }

    @Test
    @Order(2)
    void searchWithParamsPageable() {
        User user = new User(2, "Name", "qwerty@mail.ru");
        userRepository.save(user);
        Item item = new Item(2, "itemName", "item description", true, user);
        Pageable uPage = PageRequest.of(0, 1);
        Assertions.assertEquals(repository.searchWithParams("item", uPage).toString(), "[]");
        repository.save(item);
        Assertions.assertNotNull(repository.searchWithParams("item", uPage));
    }
}