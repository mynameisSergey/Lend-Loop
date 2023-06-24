package ru.practicum.shareit.booking;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.RequestRepository;
import ru.practicum.shareit.requests.RequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@TestPropertySource(properties = {"db.name=booking_test"})
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookingServiceImplTest {

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
    void createBookingAndGetOwnerBookings() {
        User user = new User(1, "Name", "a@mail.ru");
        userRepository.save(user);
        User booker = new User(2, "Name", "b@mail.ru");
        userRepository.save(booker);
        ItemRequest request = new ItemRequest(1L, "", booker, LocalDateTime.now());
        requestRepository.save(request);
        Item item = new Item(1, "itemName", "item description", true, user);
        item.setRequest(request);
        itemRepository.save(item);
        //assertThat(itemRepository.findAll(),equalTo(""));
        Booking booking = new Booking(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1), booker, item, Status.APPROVED);
        BookingDto bookingDto = BookingMapper.toBookingDto(booking);
        bookingDto.setItemId(1);
        bookingDto.setId(1);
        bookingDto.setBookerId(2);
        assertThat(bookingService.createBooking(bookingDto, booker.getId()).getItemId(),
                equalTo(BookingMapper.toBookingDto(booking).getItemId()));
        assertThat(bookingService.getOwnerBookings(0, 1, 1).get(0).getId(), equalTo(1L));

    }

    @Test
    @Order(2)
    void approval() {
        User user = new User(3, "Name", "a@mail.ru");
        userRepository.save(user);
        User booker = new User(4, "Name", "b@mail.ru");
        userRepository.save(booker);
        Item item = new Item(2, "itemName", "item description", true, user);
        itemRepository.save(item);
        Booking booking = new Booking(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1), booker, item, Status.APPROVED);
        BookingDto bookingDto = BookingMapper.toBookingDto(booking);
        bookingDto.setItemId(2);
        bookingDto.setId(2);
        bookingDto.setBookerId(4);
        bookingService.createBooking(bookingDto, booking.getBooker().getId());

    }

    @Test
    @Order(3)
    void getBooking() {
        User user = new User(5, "Name", "a@mail.ru");
        userRepository.save(user);
        User booker = new User(6, "Name", "b@mail.ru");
        userRepository.save(booker);
        Item item = new Item(3, "itemName", "item description", true, user);
        itemRepository.save(item);
        Booking booking = new Booking(LocalDateTime.now(), LocalDateTime.now(), booker, item, Status.APPROVED);
        bookingRepository.save(booking);
        assertThat(bookingService.getBookingDto(3).getId(), equalTo(3L));
    }

    @Test
    @Order(4)
    void showAllUserBookings() {
        User user = new User(7, "Name", "a@mail.ru");
        userRepository.save(user);
        User booker = new User(8, "Name", "b@mail.ru");
        userRepository.save(booker);
        Item item = new Item(4, "itemName", "item description", true, user);
        itemRepository.save(item);
        Booking booking = new Booking(LocalDateTime.now(), LocalDateTime.now(), booker, item, Status.APPROVED);
        bookingRepository.save(booking);
        assertThat(bookingService.showAllUserBookings(8, State.ALL).get(0), equalTo(booking));
    }

    @Test
    @Order(5)
    void showOwnerBookings() {
        User user = new User(9, "Name", "a@mail.ru");
        userRepository.save(user);
        User booker = new User(10, "Name", "b@mail.ru");
        userRepository.save(booker);
        Item item = new Item(5, "itemName", "item description", true, user);
        itemRepository.save(item);
        Booking booking = new Booking(LocalDateTime.now(), LocalDateTime.now(), booker, item, Status.APPROVED);
        bookingRepository.save(booking);
        assertThat(bookingService.showOwnerBookings(9, State.ALL).get(0), equalTo(booking));
    }

    @Test
    @Order(6)
    void showAll() {
        User user = new User(11, "Name", "a@mail.ru");
        User booker = new User(12, "Name", "b@mail.ru");
        Item item = new Item(6, "itemName", "item description", true, user);
        Booking booking = new Booking(LocalDateTime.now(), LocalDateTime.now(), booker, item, Status.APPROVED);
        userRepository.save(user);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);
        assertThat(bookingService.showAll(State.ALL).get(0), equalTo(booking));
    }

    @Test
    @Order(7)
    void showAllPageable() {
        User user = new User(13, "Name", "a@mail.ru");
        User booker = new User(14, "Name", "b@mail.ru");
        Item item = new Item(7, "itemName", "item description", true, user);
        Booking booking = new Booking(LocalDateTime.now(), LocalDateTime.now(), booker, item, Status.APPROVED);
        userRepository.save(user);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);
        assertThat(bookingService.showAll(State.ALL, 0, 1).get(0), equalTo(booking));
    }

    @Test
    @Order(8)
    void isBookingExist() {
        User user = new User(15, "Name", "a@mail.ru");
        User booker = new User(16, "Name", "b@mail.ru");
        Item item = new Item(8, "itemName", "item description", true, user);
        Booking booking = new Booking(LocalDateTime.now(), LocalDateTime.now(), booker, item, Status.APPROVED);
        userRepository.save(user);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);
        assertThat(bookingService.isBookingExist(7), equalTo(false));
        assertThat(bookingService.isBookingExist(8), equalTo(true));
    }

}