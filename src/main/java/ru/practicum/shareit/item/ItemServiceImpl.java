package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.WrongDataException;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;

    private final RequestRepository requestRepository;

    private final CommentRepository commentRepository;

    private final BookingRepository bookingRepository;

    private final UserRepository userRepository;

    @Override//I&T
    public ItemDto addItem(ItemDto itemDto, long ownerId) {
        User owner = userRepository.findById(ownerId).get();
        ItemRequest itemRequest = null;
        if (itemDto.getRequestId() > 0) {
            itemRequest = requestRepository.findById(itemDto.getRequestId()).get();
        }
        Item item = ItemMapper.toItem(itemDto, owner, itemRequest);
        repository.save(item);
        return ItemMapper.toItemDto(repository.findById(item.getId()).get());
    }

    @Override//I&T
    public ItemDto updateItem(ItemDto itemDto, long ownerId, long itemId) {
        if (ownerId == Objects.requireNonNull(repository.findById(itemId)).get().getOwner().getId()) {
            if (itemDto.isAvailable() == null) {
                itemDto.setAvailable(repository.findById(itemId).get().getAvailable());
            }
            if (itemDto.getName() == null) {
                itemDto.setName(repository.findById(itemId).get().getName());
            }
            if (itemDto.getDescription() == null) {
                itemDto.setDescription(repository.findById(itemId).get().getDescription());
            }
            itemDto.setId(itemId);
            User owner = userRepository.findById(ownerId).get();
            ItemRequest request = null;
            if (itemDto.getRequestId() > 0) {
                request = requestRepository.findById(itemDto.getRequestId()).get();
            }
            repository.save(ItemMapper.toItem(itemDto, owner, request));
            return ItemMapper.toItemDto(repository.findById(itemId).get());
        }
        throw new NotFoundException("");
    }

    @Override//I&T
    public ItemDto getItemDtoById(long itemId, long userId) {
        if (repository.existsById(itemId)) {
            ItemDto answer = ItemMapper.toItemDto(repository.getById(itemId));
            if (showOwnerBookings(userId, State.PAST).stream().anyMatch(x -> x.getItem().getId() == itemId) &&
                    showOwnerBookings(userId, State.FUTURE).stream().anyMatch(x -> x.getItem().getId() == itemId)
            ) {
                BookingDto lastBooking = BookingMapper.toBookingDto(showOwnerBookings(repository.getById(itemId)
                        .getOwner().getId(), State.PAST).stream().filter(x -> x.getItem().getId() == itemId).findFirst().get());
                BookingDto nextBooking = BookingMapper.toBookingDto(showOwnerBookings(repository.getById(itemId)
                        .getOwner().getId(), State.FUTURE).stream().filter(x -> x.getItem().getId() == itemId).findFirst().get());
                lastBooking.setBookerId(getBooking(lastBooking.getId()).getBooker().getId());
                nextBooking.setBookerId(getBooking(nextBooking.getId()).getBooker().getId());
                answer.setLastBooking(lastBooking);
                answer.setNextBooking(nextBooking);
            }
            if (getComment(itemId) != null) {
                System.out.println("SOOOO");
                List<CommentDto> commentDto = answer.getComments();
                Comment comment = getComment(itemId);
                commentDto.add(CommentMapper.toCommentDto(comment));
                answer.setComments(commentDto);
            }
            return answer;
        }
        return null;
    }

    @Override//I&T
    public List<ItemDto> searchItems(String text) {
        if (text.equals("")) return new LinkedList<>();
        List<Item> itemList = repository.searchWithParams(text);
        List<ItemDto> answerList = new LinkedList<>();
        for (Item item : itemList) {
            answerList.add(ItemMapper.toItemDto(item));
        }
        return answerList;
    }

    @Override//I&T
    public List<ItemDto> searchItems(String text, int from, int size) {
        Pageable uPage = PageRequest.of(from, size, Sort.by("id"));
        if (text.equals("")) return new LinkedList<>();
        List<Item> itemList = repository.searchWithParams(text, uPage);
        List<ItemDto> answerList = new LinkedList<>();
        for (Item item : itemList) {
            answerList.add(ItemMapper.toItemDto(item));
        }
        return answerList;
    }

    @Override//I&T
    public List<ItemDto> show(long id) {
        List<ItemDto> answer = new LinkedList<>();
        for (Item item : repository.findAll()) {
            if (item.getOwner().getId() == id) {
                answer.add(getItemDtoById(item.getId(), id));
            }
        }
        return answer.stream().sorted(Comparator.comparing(ItemDto::getId)).collect(Collectors.toList());
    }

    @Override//I&T
    public Page<ItemDto> show(long id, int from, int size) {
        Pageable uPage = PageRequest.of(from, size, Sort.by("id"));
        Page<Item> itemPage = repository.findAll(uPage);
        List<ItemDto> items = itemPage.get().map(ItemMapper::toItemDto).collect(Collectors.toList());
        return new PageImpl<>(items);
    }

    @Override//I&T
    public ItemDto getItemById(long itemId) {
        if (repository.findById(itemId).isPresent()) {
            return ItemMapper.toItemDto(repository.getById(itemId));
        } else throw new NotFoundException("Item with id " + itemId + " is not exist");
    }

    @Override//I&T
    public CommentDto addComment(CommentDto commentDto, long itemId, long userId) {
        Item item = repository.findById(itemId).get();
        User user = userRepository.findById(userId).get();

        Comment comment = CommentMapper.toComment(commentDto, item, user);

        for (Booking booking : showAllUserBookings(userId, State.ALL)) {
            if (booking.getItem().getId() == itemId) {
                if (booking.getEnd().isBefore(LocalDateTime.now())) {
                    commentRepository.save(comment);
                    return CommentMapper.toCommentDto(commentRepository.findById(comment.getId()).get());
                }
            }
        }
        throw new WrongDataException("The user can not set comment about the item.");
    }


    public Comment getComment(long itemId) {
        if (commentRepository.findAll().stream().anyMatch(x -> x.getItem().getId() == itemId)) {
            return commentRepository.findAll().stream().filter(x -> x.getItem().getId() == itemId).findFirst().get();
        }
        return null;
    }

    @Override
    public CommentDto getCommentDto(long itemId) {
        Comment comment = commentRepository.findById(itemId).get();
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public void removeItem(long id) {
        repository.deleteById(id);
    }

    public List<Booking> showAllUserBookings(long userId, State state) {
        return showAll(state).stream()
                .filter(x -> x.getBooker().getId() == userId)
                .sorted(new BookingComparator())
                .collect(Collectors.toList());
    }

    private List<Booking> showOwnerBookings(long userId, State state) {
        List<Booking> listOfBooking = showAll(state).stream()
                .filter(x -> x.getItem().getOwner()
                        .getId() == userId).collect(Collectors.toList());
        Collections.reverse(listOfBooking);
        return listOfBooking;
    }


    private List<Booking> showAll(State state) {
        switch (state) {
            case ALL:
                return bookingRepository.findAll().stream()
                        .sorted(Comparator.comparing(Booking::getStart))
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findAll().stream()
                        .filter(x -> x.getEnd().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findAll().stream()
                        .filter(x -> x.getStart().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findAll().stream()
                        .filter(x -> x.getStart().isBefore(LocalDateTime.now()))
                        .filter(x -> x.getEnd().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findAll().stream()
                        .filter(x -> x.getStatus().equals(Status.WAITING))
                        .collect(Collectors.toList());
            default:
                return bookingRepository.findAll().stream()
                        .filter(x -> x.getStatus().equals(Status.REJECTED))
                        .collect(Collectors.toList());
        }

    }

    private Booking getBooking(long bookingId) {
        return bookingRepository.findById(bookingId).get();
    }
}