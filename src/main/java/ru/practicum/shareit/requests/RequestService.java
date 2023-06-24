package ru.practicum.shareit.requests;

import java.util.List;

public interface RequestService {
    ItemRequest addRequest(long userId, ItemRequest request);

    List<ItemRequest> getUserRequests(long userId);

    List<ItemRequest> showRequests(int firstPage, int size, long userId);

    List<ItemRequest> showRequests(long userId);

    ItemRequest getRequestById(long requestId);

    List<ItemRequest> getAllRequests(long ownerId);
}
