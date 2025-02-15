package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private static final String XSHARERUSERID = "X-SHARE-USER-Id";

    private final RequestService requestService;

    @PostMapping
    public ItemRequestDto add(@RequestHeader(XSHARERUSERID) Long userId,
                              @Valid @RequestBody ItemRequestDto requestDto) {
        return requestService.add(userId, requestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getUserRequests(@RequestHeader(XSHARERUSERID) Long userId) {
        return requestService.getUserRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestHeader(XSHARERUSERID) Long userId,
                                               @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                               @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
        return requestService.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto get(@RequestHeader(XSHARERUSERID) Long userId,
                              @PathVariable Long requestId) {
        return requestService.getRequestById(userId, requestId);
    }
}