package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingService bookingService;

    final String SHARER = "X-Sharer-User-Id";
    @PostMapping
    public BookingDtoOut create(@RequestHeader(SHARER) Long userId,
                                @Valid @RequestBody BookingDto bookingDto) {
        log.info("POST запрос на создание нового бронирования вещи: {} от пользователя c id: {}", bookingDto, userId);
        return bookingService.add(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut updateStatus(@RequestHeader(SHARER) Long userId,
                                      @PathVariable("bookingId")
                                      Long bookingId,
                                      @RequestParam(name = "approved") Boolean approved) {
        log.info("PATCH запрос на обновление статуса бронирования вещи : {} от владельца с id: {}", bookingId, userId);
        return bookingService.update(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOut getBookingById(@RequestHeader(SHARER) Long userId,
                                        @PathVariable("bookingId")
                                        Long bookingId) {
        log.info("GET запрос на получение данных о конкретном бронировании {} от пользователся с id: {}", bookingId, userId);
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDtoOut> getAll(@RequestHeader(SHARER) Long userId,
                                      @RequestParam(value = "state", defaultValue = "ALL") String bookingState,
                                      @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                      @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        log.info("GET запрос на получение списка всех бронирований текущего пользователя с id: {} и статусом {}", userId, bookingState);
        validState(bookingState);
        return bookingService.getAll(userId, bookingState, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getAllOwner(@RequestHeader(SHARER) Long ownerId,
                                           @RequestParam(value = "state", defaultValue = "ALL") String bookingState,
                                           @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                           @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        log.info("GET запрос на получение списка всех бронирований текущего владельца с id: {} и статусом {}", ownerId, bookingState);
        validState(bookingState);
        return bookingService.getAllOwners(ownerId, bookingState, from, size);
    }

    private void validState(String bookingState) {
        BookingState state = BookingState.from(bookingState);
        if (Objects.isNull(state)) {
            throw new IllegalArgumentException(String.format("Unknown state: %s", bookingState));
        }
    }
}