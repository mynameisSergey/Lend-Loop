package ru.practicum.shareit.item;

import lombok.*;
import ru.practicum.shareit.booking.BookingDto;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class ItemResponseDto {

    private long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentResponseDto> comments;
    private Long requestId;
}
