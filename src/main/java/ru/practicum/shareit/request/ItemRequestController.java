package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import javax.validation.constraints.Min;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final RequestService requestService;
    final String SHARER = "X-Sharer-User-Id";
    @PostMapping
    public ItemRequestDto add(@RequestHeader(SHARER) Long userId,
                              @Valid @RequestBody ItemRequestDto requestDto) {
        return requestService.add(userId, requestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getUserRequests(@RequestHeader(SHARER) Long userId) {
        return requestService.getUserRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestHeader(SHARER) Long userId,
                                               @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                               @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        return requestService.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto get(@RequestHeader(SHARER) Long userId,
                              @PathVariable Long requestId) {
        return requestService.getRequestById(userId, requestId);
    }
}