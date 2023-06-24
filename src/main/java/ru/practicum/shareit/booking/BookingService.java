package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(BookingDto booking, long userId);

    void approval(long bookingId, long userId, boolean approval);

    BookingDto getBookingDto(long bookingId);

    List<Booking> showAllUserBookings(long userId, State state);

    List<Booking> showOwnerBookings(long userId, State state);

    List<Booking> showAll(State state, int from, int size);

    List<Booking> showAll(State state);

    boolean isBookingExist(long bookingId);

    List<Booking> getOwnerBookings(int firstPage, int size, long userId);

}
