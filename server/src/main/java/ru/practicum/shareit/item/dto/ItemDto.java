package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {
    private Long id;
    @NotBlank(message = "Поле name не должно быть пустым")
    private String name;
    @NotBlank(message = "Поле description не должно быть пустым")
    private String description;
    @NotNull(message = "Поле available не должно быть null")
    private Boolean available;
    private BookingItemDto lastBooking;
    private BookingItemDto nextBooking;
    private List<CommentDto> comments;
    private ItemRequest request;
}