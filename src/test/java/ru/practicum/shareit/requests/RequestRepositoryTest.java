package ru.practicum.shareit.requests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

@DataJpaTest
@TestPropertySource(properties = {"db.name=request_test"})
public class RequestRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RequestRepository repository;

    @Test
    void findNotUserRequests() {
        User user = new User(1, "Name", "a@mail.ru");
        userRepository.save(user);
        Item item = new Item(1, "itemName", "item description", true, user);
        itemRepository.save(item);
        User booker = new User(2, "Name", "b@mail.ru");
        ItemRequest request = new ItemRequest(1, "description", booker, LocalDateTime.now());
        userRepository.save(booker);
        Pageable uPage = PageRequest.of(0, 1);
        Assertions.assertEquals(repository.findNotUserRequests(1L, uPage).toString(), "[]");
        repository.save(request);
        Assertions.assertNotNull(repository.findNotUserRequests(1L, uPage));

    }
}