package ru.practicum.shareit.booking;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public BookingResponseDto createBooking(@Valid @RequestBody BookingDto bookingDto, @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return bookingService.createBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto updateBooking(@PathVariable Long bookingId,
                                            @RequestHeader(X_SHARER_USER_ID) Long userId,
                                            @RequestParam(name = "approved") Boolean approved) {
        return bookingService.updateBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                         @PathVariable Long bookingId) {
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingResponseDto> getAllBookingsByState(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                          @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                          @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                          @Positive @RequestParam(name = "size", defaultValue = "100") Integer size) {
        return bookingService.getAllBookingsByState(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllBookingsByStateAndOwner(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                                  @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                                  @Positive @RequestParam(name = "size", defaultValue = "100") Integer size) {
        return bookingService.getAllBookingsByStateAndOwner(userId, state, from, size);
    }
}