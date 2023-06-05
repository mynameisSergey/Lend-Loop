package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Data
public class BookingDto {
    private Long id;
    @NotNull
    @FutureOrPresent
    private LocalDateTime start;
    @NotNull
    @FutureOrPresent
    private LocalDateTime end;
    private StatusBooking status;
    private Long itemId;
    private Long bookerId;
    private User booker;
    private Item item;
}