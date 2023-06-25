package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private RequestRepository requestRepository;
    private User user;
    private Item item;
    private ItemRequest itemRequest;

    @Test
    void searchTest() {
        user = userRepository.save(new User(1L, "User", "user@email.ru"));
        itemRequest = requestRepository.save(new ItemRequest(1L, "description", user, LocalDateTime.now()));
        item = itemRepository.save(new Item(1L, "item", "item test", true, user, itemRequest));
        Pageable pageable = PageRequest.of(0, 1);
        Page<Item> itemList = itemRepository.search("test", pageable);
        List<Item> itemDbList = itemList.getContent();
        assertEquals(1, itemDbList.size());
        Item itemDb = itemDbList.get(0);
        assertEquals(item.getName(), itemDb.getName());
        assertEquals(item.getDescription(), itemDb.getDescription());
        assertEquals(item.getAvailable(), itemDb.getAvailable());
        assertEquals(item.getOwner(), itemDb.getOwner());
        assertEquals(item.getRequest(), itemDb.getRequest());
    }
}
