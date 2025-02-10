package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@Valid @RequestBody BookingDto bookingDto, @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return bookingService.createBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(@PathVariable Long bookingId,
                                    @RequestHeader(X_SHARER_USER_ID) Long userId,
                                    @RequestParam("approved") Boolean approved) {
        return bookingService.updateBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                 @PathVariable Long bookingId) {
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsByState(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                  @RequestParam(name = "state", required = false, defaultValue = "ALL") String state) {
        return bookingService.getAllBookingsByState(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsByStateAndOwner(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                          @RequestParam(name = "state", required = false, defaultValue = "ALL") String state) {
        return bookingService.getAllBookingsByStateAndOwner(userId, state);
    }
}