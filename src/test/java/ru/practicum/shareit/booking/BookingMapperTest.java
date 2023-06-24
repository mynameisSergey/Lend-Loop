package ru.practicum.shareit.booking;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@TestPropertySource(properties = {"db.name=booking_test"})
public class BookingMapperTest {

    @Test
    void toBookingDto() {
        var item = new Item();
        item.setId(3L);
        item.setName("Tool");

        var user = new User();
        user.setId(2L);
        user.setName("Test");
        item.setOwner(user);
        var original = new Booking();
        original.setId(1L);
        original.setStatus(Status.APPROVED);
        original.setStart(LocalDateTime.now());
        original.setEnd(LocalDateTime.now());
        original.setBooker(user);
        original.setItem(item);
        var result = BookingMapper.toBookingDto(original);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(original.getId(), result.getId());
        Assertions.assertEquals(original.getStart(), result.getStart());
        Assertions.assertEquals(original.getEnd(), result.getEnd());
    }

    @Test
    void toBooking() {
        var item = new Item();
        item.setId(3L);
        item.setName("Tool");
        var user = new User();
        user.setId(2L);
        user.setName("Test");
        var original = new BookingDto(1L, item.getId(), LocalDateTime.now(), LocalDateTime.now());
        var result = BookingMapper.toBooking(original, user, item);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getItem());
        Assertions.assertNotNull(result.getBooker());
    }
}