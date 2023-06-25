package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final RequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public ItemRequestResponseDto createRequest(ItemRequestDto itemRequestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Пользователь не найден" + userId)));
        ItemRequest newItemRequest = RequestMapper.toItemRequest(itemRequestDto, user);
        ItemRequest createdItemRequest = itemRequestRepository.save(newItemRequest);
        return RequestMapper.toItemRequestResponseDto(createdItemRequest);
    }

    @Override
    public ItemRequestResponseDto getRequest(Long id, Long userId) {
        ItemRequest itemRequest = itemRequestRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Запрос не найден" + id));
        userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Пользователь не найден" + userId)));
        List<ItemDto> items = itemRepository.findAllByRequestId(id).stream()
                .map(ItemMapper::toItemDto)
                .collect(toList());
        ItemRequestResponseDto itemRequestResponseDto = RequestMapper.toItemRequestResponseDto(itemRequest);
        itemRequestResponseDto.setItems(items);
        return itemRequestResponseDto;
    }

    @Override
    public List<ItemRequestResponseDto> getUserRequests(Long userId, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Пользователь не найден" + userId)));
        Pageable pageable;
        pageable = PageRequest.of(from, size);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterId(userId, pageable).stream()
                .collect(toList());
        return getItemRequestResponseDtos(itemRequests);
    }

    @Override
    public List<ItemRequestResponseDto> getAllRequests(Long userId, Integer from, Integer size) {
        Pageable pageable;
        userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Пользователь не найден" + userId)));
        pageable = PageRequest.of(from, size, Sort.by("created").descending());
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterIdIsNot(userId, pageable).stream()
                .filter(itemRequest -> !itemRequest.getRequester().getId().equals(userId))
                .collect(toList());
        return getItemRequestResponseDtos(itemRequests);
    }

    private List<ItemRequestResponseDto> getItemRequestResponseDtos(List<ItemRequest> itemRequests) {
        Map<Long, List<ItemDto>> itemsByRequest = itemRepository.findAllByRequestIn(itemRequests)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(groupingBy(ItemDto::getRequestId, toList()));
        List<ItemRequestResponseDto> itemRequestList = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            ItemRequestResponseDto itemRequestResponseDto = RequestMapper.toItemRequestResponseDto(itemRequest);
            itemRequestResponseDto.setItems(itemsByRequest.getOrDefault(itemRequest.getId(), Collections.emptyList()));
            itemRequestList.add(itemRequestResponseDto);
        }
        return itemRequestList;
    }
}