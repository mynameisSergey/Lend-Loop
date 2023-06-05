package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(BookingDto bookingDto, Long userId);

    BookingDto getBooking(Long id, Long userId);

    BookingDto updateBooking(Long bookingId, Long userId, Boolean approved);

    List<BookingDto> getAllBookingsByState(Long userId, String state);

    List<BookingDto> getAllBookingsByStateAndOwner(Long userId, String stringState);
}

