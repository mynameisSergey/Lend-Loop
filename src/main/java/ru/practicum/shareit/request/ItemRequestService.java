package ru.practicum.shareit.request;

import java.util.List;

public interface ItemRequestService {
    ItemRequestResponseDto createRequest(ItemRequestDto itemRequestDto, Long userId);

    ItemRequestResponseDto getRequest(Long id, Long userId);

    List<ItemRequestResponseDto> getUserRequests(Long userId, Integer from, Integer size);

    List<ItemRequestResponseDto> getAllRequests(Long userId, Integer from, Integer size);
}