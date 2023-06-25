package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import ru.practicum.shareit.booking.BookingDto;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.Booking;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null);
    }

    public static ItemResponseDto toItemResponseDto(Item item) {
        return new ItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                null,
                null,
                new ArrayList<>(),
                item.getRequest() != null ? item.getRequest().getId() : null);
    }

    public static Item toItem(Item item, ItemDto itemDto) {
        item.setId(itemDto.getId());
        if (!StringUtils.isBlank(itemDto.getName())) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        if (!StringUtils.isBlank(itemDto.getDescription())) {
            item.setDescription(itemDto.getDescription());
        }
        return item;
    }

    public static ItemResponseDto toMap(Item item, List<CommentResponseDto> comments, Booking lastBooking, Booking nextBooking) {
        BookingDto nextBookingDto = null;
        BookingDto lastBookingDto = null;
        if (lastBooking != null) {
            lastBookingDto = BookingMapper.toBookingDto(lastBooking);
        }
        if (nextBooking != null) {
            nextBookingDto = BookingMapper.toBookingDto(nextBooking);
        }
        return new ItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBookingDto,
                nextBookingDto,
                comments,
                item.getRequest() != null ? item.getRequest().getId() : null);
    }
}