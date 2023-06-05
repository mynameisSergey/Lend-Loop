package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Optional;

public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .bookerId(booking.getBooker().getId())
                .build();
    }

    public static void toBooking(Booking booking, BookingDto bookingDto) {
        booking.setId(bookingDto.getId());
        Optional.ofNullable(bookingDto.getStart()).ifPresent(booking::setStart);
        Optional.ofNullable(bookingDto.getEnd()).ifPresent(booking::setEnd);
        Optional.ofNullable(bookingDto.getStatus()).ifPresent(booking::setStatus);
    }
}