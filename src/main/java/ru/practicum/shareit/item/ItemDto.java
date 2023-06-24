package ru.practicum.shareit.item;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.BookingDto;
import ru.practicum.shareit.comment.CommentDto;

import java.util.LinkedList;
import java.util.List;

/**
 * // TODO .
 */
@Data
@Getter
@Setter
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available = null;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentDto> comments = new LinkedList<>();
    private long requestId;

    public ItemDto() {

    }

    public ItemDto(long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;

    }

    public ItemDto(long id, String name, String description, Boolean available, long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;

    }

    public Boolean isAvailable() {
        return available;
    }

}