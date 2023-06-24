package ru.practicum.shareit.booking;

import org.junit.jupiter.api.*;
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
@TestPropertySource(properties = {"db.name=booking_test"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookingRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository repository;


    @Test
    @Order(1)
    void findByBookerId() {
        User user = new User(1, "Name", "qwerty@mail.ru");
        userRepository.save(user);
        Item item = new Item(1, "itemName", "item description", true, user);
        itemRepository.save(item);
        Booking booking = new Booking(LocalDateTime.now(), LocalDateTime.now(), user, item, Status.APPROVED);
        booking.setId(1);
        Pageable uPage = PageRequest.of(0, 1);
        Assertions.assertEquals(repository.findByBookerId(1L, uPage).toString(), "[]");
        repository.save(booking);
        Assertions.assertNotNull(repository.findByBookerId(1L, uPage));
    }

    @Test
    @Order(2)
    void getOwnerBookings() {
        User user = new User(2, "Name", "qwerty@mail.ru");
        userRepository.save(user);
        Item item = new Item(2, "itemName", "item description", true, user);
        itemRepository.save(item);
        Booking booking = new Booking(LocalDateTime.now(), LocalDateTime.now(), user, item, Status.APPROVED);
        booking.setId(2);
        Pageable uPage = PageRequest.of(0, 1);
        Assertions.assertEquals(repository.getOwnerBookings(2L, uPage).toString(), "[]");
        repository.save(booking);
        Assertions.assertNotNull(repository.getOwnerBookings(2L, uPage));
    }
}