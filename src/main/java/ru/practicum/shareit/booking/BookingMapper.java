package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .bookerId(booking.getBooker().getId())
                .build();
    }

    public static void toBooking(Booking booking, BookingDto bookingDto) {
        booking.setId(bookingDto.getId());
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setStatus(StatusBooking.WAITING);
    }

    public static BookingResponseDto toBookingResponseDto(Booking booking) {
        if (booking != null) {
            BookingResponseDto.Booker booker = new BookingResponseDto.Booker(booking.getBooker().getId(), booking.getBooker().getName());
            BookingResponseDto.Item item = new BookingResponseDto.Item(booking.getItem().getId(), booking.getItem().getName(), booking.getItem().getOwner().getId());
            return new BookingResponseDto(booking.getId(),
                    booking.getStart(),
                    booking.getEnd(),
                    booking.getStatus(),
                    booker,
                    item);
        } else {
            return null;
        }
    }
}