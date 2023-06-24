package ru.practicum.shareit.requests;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@TestPropertySource(properties = {"db.name=request_test"})
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RequestServiceImplTest {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Test
    @Order(1)
    void addRequest() {
        User user = new User(1, "Name", "a@mail.ru");
        User booker = new User(2, "Name", "b@mail.ru");
        Item item = new Item(1, "itemName", "item description", true, user);
        userRepository.save(user);
        userRepository.save(booker);
        itemRepository.save(item);
        ItemRequest itemRequest = new ItemRequest(1, "d", booker, LocalDateTime.now());
        assertThat(requestService.addRequest(1, itemRequest).toString(),
                equalTo(itemRequest.toString()));
    }

    @Test
    @Order(2)
    void getUserRequests() {
        User user = new User(3, "Name", "a@mail.ru");
        userRepository.save(user);
        User booker = new User(4, "Name", "b@mail.ru");
        userRepository.save(booker);
        Item item = new Item(2, "itemName", "item description", true, user);
        itemRepository.save(item);
        ItemRequest itemRequest = new ItemRequest(2, "d", booker, LocalDateTime.now());
        requestService.addRequest(3, itemRequest);
        assertThat(requestService.getUserRequests(4).get(0).toString(),
                equalTo(itemRequest.toString()));
    }

    @Test
    @Order(3)
    void showRequests() {
        User user = new User(5, "Name", "a@mail.ru");
        User booker = new User(6, "Name", "b@mail.ru");
        Item item = new Item(3, "itemName", "item description", true, user);
        ItemRequest itemRequest = new ItemRequest(3, "d", booker, LocalDateTime.now());
        userRepository.save(user);
        userRepository.save(booker);
        itemRepository.save(item);
        requestService.addRequest(5, itemRequest);
        assertThat(requestService.showRequests(6).get(0).toString(),
                equalTo(itemRequest.toString()));
    }

    @Test
    @Order(4)
    void showRequestsPageable() {
        User user = new User(7, "Name", "a@mail.ru");
        User booker = new User(8, "Name", "b@mail.ru");
        Item item = new Item(4, "itemName", "item description", true, user);
        ItemRequest itemRequest = new ItemRequest(4, "d", booker, LocalDateTime.now());
        userRepository.save(user);
        userRepository.save(booker);
        itemRepository.save(item);
        requestService.addRequest(7, itemRequest);
        assertThat(requestService.showRequests(0, 1, 7).get(0).toString(),
                equalTo(itemRequest.toString()));
    }

    @Test
    @Order(5)
    void getRequestById() {
        User user = new User(9, "Name", "a@mail.ru");
        User booker = new User(10, "Name", "b@mail.ru");
        Item item = new Item(5, "itemName", "item description", true, user);
        ItemRequest itemRequest = new ItemRequest(5, "d", booker, LocalDateTime.now());
        userRepository.save(user);
        userRepository.save(booker);
        itemRepository.save(item);
        requestService.addRequest(9, itemRequest);
        assertThat(requestService.getRequestById(5).toString(),
                equalTo(itemRequest.toString()));
    }

    @Test
    @Order(6)
    void getAllRequests() {
        User user = new User(11, "Name", "a@mail.ru");
        User booker = new User(12, "Name", "b@mail.ru");
        Item item = new Item(6, "itemName", "item description", true, user);
        ItemRequest itemRequest = new ItemRequest(6, "d", booker, LocalDateTime.now());
        userRepository.save(user);
        userRepository.save(booker);
        itemRepository.save(item);
        requestService.addRequest(11, itemRequest);
        assertThat(requestService.getAllRequests(11).get(0).toString(),
                equalTo(itemRequest.toString()));
    }

}