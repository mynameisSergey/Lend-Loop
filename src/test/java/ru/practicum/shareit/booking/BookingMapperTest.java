package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingMapperTest {

    private final Booking booking = Booking.builder()
            .id(1L)
            .booker(User.builder().id(1L).name("name").email("email@email.com").build())
            .item(new Item())
            .start(LocalDateTime.now().plusMinutes(5))
            .end(LocalDateTime.now().plusMinutes(10))
            .status(BookingStatus.WAITING)
            .build();

    @Test
    void toBookingItemDtoTest() {
        BookingItemDto actualBookingItemDto = BookingMapper.toBookingItemDto(booking);

        assertEquals(1L, actualBookingItemDto.getId());
        assertEquals(1L, actualBookingItemDto.getBookerId());
    }
}