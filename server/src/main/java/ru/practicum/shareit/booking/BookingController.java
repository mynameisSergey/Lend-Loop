package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Objects;
@RequiredArgsConstructor
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String XSHARERUSERID = "X-SHARE-USER-Id";

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDtoOut> create(@RequestHeader(XSHARERUSERID) Long userId,
                                               @Valid @RequestBody BookingDto bookingDto) {
        log.info("POST запрос на создание нового бронирования вещи: {} от пользователя c id: {}", bookingDto, userId);
        return ResponseEntity.ok(bookingService.add(userId, bookingDto));
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDtoOut> updateStatus(@RequestHeader(XSHARERUSERID) Long userId,
                                      @PathVariable("bookingId")
                                      Long bookingId,
                                      @RequestParam(name = "approved") Boolean approved) {
        log.info("PATCH запрос на обновление статуса бронирования вещи : {} от владельца с id: {}", bookingId, userId);
        return ResponseEntity.ok(bookingService.update(userId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDtoOut> getBookingById(@RequestHeader(XSHARERUSERID) Long userId,
                                        @PathVariable("bookingId")
                                        Long bookingId) {
        log.info("GET запрос на получение данных о конкретном бронировании {} от пользователся с id: {}", bookingId, userId);
        return ResponseEntity.ok(bookingService.getBookingById(userId, bookingId));
    }

    @GetMapping
    public ResponseEntity<List<BookingDtoOut>> getAll(@RequestHeader(XSHARERUSERID) Long userId,
                                      @RequestParam(value = "state", defaultValue = "ALL") String bookingState,
                                      @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                      @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("GET запрос на получение списка всех бронирований текущего пользователя с id: {} и статусом {}",
                userId, bookingState);
        validState(bookingState);
        return ResponseEntity.ok(bookingService.getAll(userId, bookingState, from, size));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDtoOut>> getAllOwner(@RequestHeader(XSHARERUSERID) Long ownerId,
                                           @RequestParam(value = "state", defaultValue = "ALL") String bookingState,
                                           @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                           @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("GET запрос на получение списка всех бронирований текущего владельца с id: {} и статусом {}", ownerId, bookingState);
        validState(bookingState);
        return ResponseEntity.ok(bookingService.getAllOwner(ownerId, bookingState, from, size));
    }

    private void validState(String bookingState) {
        BookingState state = BookingState.from(bookingState);
        if (Objects.isNull(state)) {
            throw new IllegalArgumentException(String.format("Unknown state: %s", bookingState));
        }
    }
}
