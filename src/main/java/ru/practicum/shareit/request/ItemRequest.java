package ru.practicum.shareit.request;

import ru.practicum.shareit.user.User;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    private Long id;
    @NotEmpty
    private String description;
    private User requestor;
    private LocalDateTime created;
}
