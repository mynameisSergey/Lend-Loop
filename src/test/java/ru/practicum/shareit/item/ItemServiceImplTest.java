package ru.practicum.shareit.item;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@Transactional
@TestPropertySource(properties = {"db.name=item_test"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemServiceImplTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Test
    @Order(1)
    void addItem() {
        User user = new User(1, "Name", "qwerty@mail.ru");
        userRepository.save(user);
        ItemRequest request = new ItemRequest(1, "1", user, LocalDateTime.now());
        requestRepository.save(request);
        ItemDto itemDto = new ItemDto(1, "1", "1", true);
        itemDto.setRequestId(1);
        assertThat(itemService.addItem(itemDto, 1), equalTo(itemDto));
    }

    @Test
    @Order(2)
    void updateItem() {
        User user = new User(2, "Name", "qwerty@mail.ru");
        userRepository.save(user);
        User booker = new User(3, "Name", "b@mail.ru");
        userRepository.save(booker);
        ItemRequest request = new ItemRequest(2, "1", booker, LocalDateTime.now());
        requestRepository.save(request);
        Item item = new Item(2, "itemName", "item description", true, user);
        item.setRequest(request);
        itemRepository.save(item);
        ItemDto itemDto = new ItemDto(2, "1", "1", true);
        itemDto.setRequestId(2);
        assertThat(itemService.updateItem(itemDto, 2, 2).getId(), equalTo(2L));

    }

    @Test
    @Order(3)
    void getItemDtoById() {
        User user = new User(4, "Name", "qwerty@mail.ru");
        userRepository.save(user);
        User booker = new User(5, "Name", "b@mail.ru");
        userRepository.save(booker);
        ItemRequest request = new ItemRequest(3, "1", booker, LocalDateTime.now());
        requestRepository.save(request);
        Item item = new Item(3, "itemName", "item description", true, user);
        item.setRequest(request);
        itemRepository.save(item);
        ItemDto itemDto = new ItemDto(3, "1", "1", true);
        itemDto.setRequestId(3);
        assertThat(itemService.getItemById(3).getId(), equalTo(3L));
    }

    @Test
    @Order(4)
    void searchItems() {
        User user = new User(6, "Name", "qwerty@mail.ru");
        userRepository.save(user);
        User booker = new User(7, "Name", "b@mail.ru");
        userRepository.save(booker);
        ItemRequest request = new ItemRequest(4, "1", booker, LocalDateTime.now());
        requestRepository.save(request);
        Item item = new Item(4, "itemName", "item description", true, user);
        item.setRequest(request);
        itemRepository.save(item);
        ItemDto itemDto = new ItemDto(4, "1", "1", true);
        itemDto.setRequestId(2);
        assertThat(itemService.searchItems("item").get(0).getId(), equalTo(itemDto.getId()));
    }

    @Test
    @Order(5)
    void showPageable() {
        User user = new User(8, "Name", "qwerty@mail.ru");
        userRepository.save(user);
        User booker = new User(9, "Name", "b@mail.ru");
        userRepository.save(booker);
        ItemRequest request = new ItemRequest(5, "1", booker, LocalDateTime.now());
        requestRepository.save(request);
        Item item = new Item(5, "itemName", "item description", true, user);
        item.setRequest(request);
        itemRepository.save(item);
        ItemDto itemDto = new ItemDto(5, "1", "1", true);
        itemDto.setRequestId(5);
        assertThat(itemService.show(5, 0, 2).getTotalElements(), equalTo(1L));
    }

    @Test
    @Order(6)
    void show() {
        User user = new User(10, "Name", "qwerty@mail.ru");
        userRepository.save(user);
        User booker = new User(11, "Name", "b@mail.ru");
        userRepository.save(booker);
        ItemRequest request = new ItemRequest(6, "1", booker, LocalDateTime.now());
        requestRepository.save(request);
        Item item = new Item(6, "itemName", "item description", true, user);
        item.setRequest(request);
        itemRepository.save(item);
        ItemDto itemDto = new ItemDto(6, "1", "1", true);
        itemDto.setRequestId(6);
        assertThat(itemService.show(10).get(0).getId(),
                equalTo(6L));
    }

    @Test
    @Order(7)
    void searchItemsPageable() {
        User user = new User(12, "Name", "qwerty@mail.ru");
        userRepository.save(user);
        User booker = new User(13, "Name", "b@mail.ru");
        userRepository.save(booker);
        ItemRequest request = new ItemRequest(7, "1", booker, LocalDateTime.now());
        requestRepository.save(request);
        Item item = new Item(7, "itemName", "item description", true, user);
        item.setRequest(request);
        itemRepository.save(item);
        ItemDto itemDto = new ItemDto(7, "1", "1", true);
        itemDto.setRequestId(7);
        assertThat(itemService.searchItems("item", 0, 2).get(0).getId(), equalTo(7L));

    }

    @Test
    @Order(8)
    void getItemById() {
        User user = new User(14, "Name", "qwerty@mail.ru");
        userRepository.save(user);
        User booker = new User(15, "Name", "b@mail.ru");
        userRepository.save(booker);
        ItemRequest request = new ItemRequest(8, "1", booker, LocalDateTime.now());
        requestRepository.save(request);
        Item item = new Item(8, "itemName", "item description", true, user);
        item.setRequest(request);
        itemRepository.save(item);
        ItemDto itemDto = new ItemDto(8, "1", "1", true);
        itemDto.setRequestId(8);
        assertThat(itemService.getItemById(8).getId(), equalTo(8L));
    }

    @Test
    @Order(9)
    void addComment() throws InterruptedException {
        User user = new User(16, "Name", "qwerty@mail.ru");
        userRepository.save(user);
        User booker = new User(17, "Name", "b@mail.ru");
        userRepository.save(booker);
        ItemRequest request = new ItemRequest(9, "1", booker, LocalDateTime.now());
        requestRepository.save(request);
        Item item = new Item(9, "itemName", "item description", true, user);
        item.setRequest(request);
        itemRepository.save(item);
        ItemDto itemDto = new ItemDto(9, "1", "1", true);
        itemDto.setRequestId(9);
        CommentDto comment = new CommentDto(1, "comment", LocalDate.now(), 10, "Mark");
        Booking booking = new Booking(LocalDateTime.now(), LocalDateTime.now(), booker, item, Status.APPROVED);
        bookingRepository.save(booking);
        Thread.sleep(500);
        assertThat(itemService.addComment(comment, 9, 17).getId(),
                equalTo(1L));
    }

    @Test
    @Order(10)
    void getComment() throws InterruptedException {
        User user = new User(18, "Name", "qwerty@mail.ru");
        userRepository.save(user);
        User booker = new User(19, "Name", "b@mail.ru");
        userRepository.save(booker);
        ItemRequest request = new ItemRequest(10, "1", booker, LocalDateTime.now());
        requestRepository.save(request);
        Item item = new Item(10, "itemName", "item description", true, user);
        item.setRequest(request);
        itemRepository.save(item);
        ItemDto itemDto = new ItemDto(10, "1", "1", true);
        itemDto.setRequestId(10);
        CommentDto comment = new CommentDto(2, "comment", LocalDate.now(), 10, "Mark");
        Booking booking = new Booking(LocalDateTime.now(), LocalDateTime.now(), booker, item, Status.APPROVED);
        Thread.sleep(500);
        bookingRepository.save(booking);
        itemService.addComment(comment, 10, 19);
        assertThat(itemService.getCommentDto(2).getId(), equalTo(2L));
    }

}