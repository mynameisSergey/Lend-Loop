package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public ItemResponseDto createItem(ItemDto itemDto, Long userId) {
        Item newItem = ItemMapper.toItem(new Item(), itemDto);
        newItem.setOwner(userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Собственник не найден")));
        if (itemDto.getRequestId() != null) {
            var itemRequest = requestRepository.findById(itemDto.getRequestId());
            itemRequest.ifPresent(newItem::setRequest);
        }
        Item createdItem = itemRepository.save(newItem);
        return ItemMapper.toItemResponseDto(createdItem);
    }

    @Override
    @Transactional
    public ItemResponseDto updateItem(ItemDto itemDto, Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException("Вещь не найдена"));
        if (!userId.equals(item.getOwner().getId())) {
            throw new ObjectNotFoundException("Собственник не найден");
        }
        ItemMapper.toItem(item, itemDto);
        item.setId(itemId);
        return ItemMapper.toItemResponseDto(item);
    }

    @Override
    public ItemResponseDto getItemById(Long id, Long userId) {
        Item itemDb = itemRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Вещь не найдена" + id)));
        return addBookingsCommentsItem(itemDb, userId);
    }

    @Override
    public List<ItemResponseDto> getItemsByUserId(Long userId, Integer from, Integer size) {
        Pageable pageable;
        pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Пользователь не найден" + userId)));
        List<Item> items = itemRepository.findAllByOwnerId(userId, pageable).getContent();
        List<Comment> comments = commentRepository.findAllByItemIn(items);
        Map<Long, List<CommentResponseDto>> commentsByItemIds = comments.stream()
                .map(CommentMapper::toCommentResponseDto)
                .collect(Collectors.groupingBy(CommentResponseDto::getItemId, Collectors.toList()));
        LocalDateTime now = LocalDateTime.now();
        List<Booking> lastBookings = bookingRepository.findAllByItemInAndStartLessThanEqualAndStatusIsOrderByEndDesc(items, now, StatusBooking.APPROVED);
        Map<Long, Booking> lastBookingsByItemIds = lastBookings.stream()
                .collect(Collectors.toMap(booking -> booking.getItem().getId(), Function.identity(), (booking1, booking2) -> booking1));
        List<Booking> nextBookings = bookingRepository.findAllByItemInAndStartIsAfterAndStatusOrderByStart(
                items, now, StatusBooking.APPROVED);
        Map<Long, Booking> nextBookingsByItemIds = nextBookings.stream()
                .collect(Collectors.toMap(booking -> booking.getItem().getId(), Function.identity(), (booking1, booking2) -> booking1));
        return items.stream()
                .map(item -> ItemMapper.toMap(item, commentsByItemIds.get(item.getId()),
                        lastBookingsByItemIds.get(item.getId()), nextBookingsByItemIds.get(item.getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeItemById(Long id) {
        itemRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Вещь не найдена:" + id));
        itemRepository.deleteById(id);
    }

    public List<ItemResponseDto> searchItems(String text, Integer from, Integer size) {
        Pageable pageable;
        pageable = PageRequest.of(from / size, size);
        if (StringUtils.isBlank(text)) {
            return Collections.emptyList();
        }
        return itemRepository.search(text, pageable)
                .stream()
                .map(ItemMapper::toItemResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentResponseDto createComment(CommentDto commentDto, Long userId, Long itemId) {
        Comment newComment = new Comment();
        CommentMapper.toComment(newComment, commentDto);
        newComment.setCreated(LocalDateTime.now());
        newComment.setAuthor(userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Пользователь не найден" + userId))));
        newComment.setItem(itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Вещь не найдена:" + itemId))));
        Booking booking = bookingRepository.findFirstByItemAndBookerAndEndIsBeforeAndStatusIs(newComment.getItem(), newComment.getAuthor(), LocalDateTime.now(), StatusBooking.APPROVED);
        if (booking == null) {
            throw new BadRequestException("Вещь не была забронирована");
        }
        Comment createdComment = commentRepository.save(newComment);
        return CommentMapper.toCommentResponseDto(createdComment);
    }

    private ItemResponseDto addBookingsCommentsItem(Item itemDto, Long userId) {
        ItemResponseDto itemResponseDto = ItemMapper.toItemResponseDto(itemDto);
        var lastBooking = bookingRepository.findFirstByItemAndStartLessThanEqualAndStatusIsOrderByEndDesc(itemDto, LocalDateTime.now(), StatusBooking.APPROVED);
        if (lastBooking != null && userId.equals(itemDto.getOwner().getId())) {
            itemResponseDto.setLastBooking(BookingMapper.toBookingDto(lastBooking));
        }
        var nextBooking = bookingRepository.findFirstByItemAndStartIsAfterAndStatusIsOrderByStart(itemDto, LocalDateTime.now(), StatusBooking.APPROVED);
        if (nextBooking != null && userId.equals(itemDto.getOwner().getId())) {
            itemResponseDto.setNextBooking(BookingMapper.toBookingDto(nextBooking));
        }
        List<CommentResponseDto> comments = commentRepository.findAllByItem(itemDto).stream()
                .map(CommentMapper::toCommentResponseDto)
                .collect(Collectors.toList());
        itemResponseDto.setComments(comments);

        return itemResponseDto;
    }
}