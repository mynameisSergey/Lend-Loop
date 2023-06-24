package ru.practicum.shareit.requests;

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
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping()
    public ItemRequestResponseDto createRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                                @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemRequestService.createRequest(itemRequestDto, userId);
    }

    @GetMapping()
    public List<ItemRequestResponseDto> getUserRequests(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                        @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                        @Positive @RequestParam(name = "size", defaultValue = "100") Integer size) {
        return itemRequestService.getUserRequests(userId, from, size);
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDto> getAllRequest(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                      @Positive @RequestParam(name = "size", defaultValue = "100") Integer size) {
        return itemRequestService.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto getRequest(@PathVariable Long requestId,
                                             @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemRequestService.getRequest(requestId, userId);
    }
}