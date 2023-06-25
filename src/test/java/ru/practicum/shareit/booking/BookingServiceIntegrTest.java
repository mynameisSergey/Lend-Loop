package ru.practicum.shareit.booking;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.request.ItemRequest;
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
@SpringJUnitConfig({ShareItApp.class, BookingServiceImpl.class})
public class BookingServiceIntegrTest {

    private final EntityManager em;
    private final ItemServiceImpl itemService;
    private final UserServiceImpl userService;
    private final BookingServiceImpl bookingService;

    @Test
    void createBookingTest() {
        UserDto userDto = new UserDto(1L, "User1", "user1@mail.ru");
        User user = new User(1L, "User1", "user1@mail.ru");
        UserDto userDto1 = new UserDto(2L, "User2", "user2@mail.ru");
        User user1 = new User(2L, "User2", "user2@mail.ru");
        userService.createUser(userDto);
        userService.createUser(userDto1);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        Item item = new Item(1L, "Item", "Item description", true, user1, new ItemRequest());
        ItemDto itemDto = new ItemDto(1L, "Item", "Item description", true, 1L);
        itemService.createItem(itemDto, 1L);
        BookingDto bookingDto = new BookingDto(1L, start, end, 1L, 1L);
        bookingService.createBooking(bookingDto, userDto1.getId());
        TypedQuery<Booking> query = em.createQuery("Select i from Booking i where i.id = : id", Booking.class);
        Booking booking = query.setParameter("id", bookingDto.getId()).getSingleResult();
        assertThat(booking.getStart(), equalTo(bookingDto.getStart()));
        assertThat(booking.getEnd(), equalTo(bookingDto.getEnd()));
    }
}
