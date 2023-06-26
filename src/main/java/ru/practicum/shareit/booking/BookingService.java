package ru.practicum.shareit.booking;

import java.util.List;


public interface BookingService {
    BookingDtoOut add(Long userId, BookingDto bookingDto);

    BookingDtoOut update(Long userId, Long bookingId, Boolean approved);

    BookingDtoOut getBookingById(Long userId, Long bookingId);

    List<BookingDtoOut> getAll(Long userId, String state, Integer from, Integer size);

    List<BookingDtoOut> getAllOwners(Long userId, String state, Integer from, Integer size);
}