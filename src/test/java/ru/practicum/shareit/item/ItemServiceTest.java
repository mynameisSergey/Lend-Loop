package ru.practicum.shareit.item;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private RequestRepository requestRepository;
    private ItemServiceImpl itemService;
    private ItemDto itemDto;
    private Item item;
    private User user;
    private Booking booking;
    private List<Booking> lastBooking;
    private List<Booking> nextBooking;
    private ItemRequest itemRequest;
    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    void beforeEach() {
        itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository,
                requestRepository);
        user = new User(1L, "user", "user@email.ru");
        item = new Item(1L, "item", "item test", true, user, new ItemRequest());
        comment = new Comment(1L, "Comment", item, user, LocalDateTime.now());
        commentDto = new CommentDto(1L, "Comment");
        itemDto = new ItemDto(1L, "item", "item test", true, 1L);
        booking = new Booking(2L, LocalDateTime.now(), LocalDateTime.now().minusDays(1), item, user,
                StatusBooking.APPROVED);
        itemRequest = new ItemRequest(1L, "description", user, LocalDateTime.now());
        lastBooking = new ArrayList<>();
        lastBooking.add(new Booking(5L, LocalDateTime.now().minusDays(6), LocalDateTime.now().minusDays(1), item, user,
                StatusBooking.APPROVED));
        nextBooking = new ArrayList<>();
        nextBooking.add(new Booking(7L, LocalDateTime.now(), LocalDateTime.now().minusDays(1), item, user,
                StatusBooking.APPROVED));
    }

    @Order(1)
    @Test
    void createItemTest() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(requestRepository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.save(any())).thenReturn(item);
        ItemResponseDto itemDb = itemService.createItem(itemDto, 1L);
        assertEquals(itemDb.getId(), itemDto.getId());
        assertEquals(itemDb.getName(), itemDto.getName());
        assertEquals(itemDb.getDescription(), itemDto.getDescription());
        assertThrows(ObjectNotFoundException.class, () -> itemService.createItem(itemDto, 2L));
    }

    @Order(2)
    @Test
    void updateItemTest() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        ItemResponseDto itemDb = itemService.updateItem(itemDto, 1L, 1L);
        assertEquals(item.getId(), itemDb.getId());
        assertEquals(item.getName(), itemDb.getName());
        assertEquals(item.getDescription(), itemDb.getDescription());
        assertThrows(ObjectNotFoundException.class, () -> itemService.updateItem(itemDto, 1L, 2L));
    }

    @Order(3)
    @Test
    void getItemByIdTest() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        ItemResponseDto itemResponseDto = itemService.getItemById(1L, 1L);
        assertEquals(item.getId(), itemResponseDto.getId());
        assertEquals(item.getName(), itemResponseDto.getName());
        assertEquals(item.getDescription(), itemResponseDto.getDescription());
        assertThrows(ObjectNotFoundException.class, () -> itemService.getItemById(2L, 2L));
    }

    @Order(4)
    @Test
    void getItemsByUserIdTest() {
        Pageable pageable = PageRequest.of(1, 1, Sort.by(Sort.Direction.ASC, "id"));
        Item item = new Item(1L, "item", "item test", true, user, null);
        List<Item> items = new ArrayList<>(Collections.singletonList(item));
        Page<Item> pagedResponse = new PageImpl<>(items);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findAllByOwnerId(1L, pageable)).thenReturn(pagedResponse);
        when(bookingRepository.findAllByItemInAndStartLessThanEqualAndStatusIsOrderByEndDesc(any(List.class),
                any(LocalDateTime.class), any(StatusBooking.class))).thenReturn(lastBooking);
        when(bookingRepository.findAllByItemInAndStartIsAfterAndStatusOrderByStart(any(List.class),
                any(LocalDateTime.class), any(StatusBooking.class))).thenReturn(nextBooking);
        final List<ItemResponseDto> itemDtoList = itemService.getItemsByUserId(1L, 1, 1);
        assertNotNull(itemDtoList);
        assertEquals(1, itemDtoList.size());
        ItemResponseDto itemDto = itemDtoList.get(0);
        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
        assertThrows(ObjectNotFoundException.class, () -> itemService.getItemsByUserId(2L, 1, 1));
    }

    @Order(5)
    @Test
    void getItemsByUserIdTestWithoutBooking() {
        Pageable pageable = PageRequest.of(1, 1, Sort.by(Sort.Direction.ASC, "id"));
        Item item = new Item(1L, "item", "item test", true, user, null);
        List<Item> items = new ArrayList<>(Collections.singletonList(item));
        Page<Item> pagedResponse = new PageImpl<>(items);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findAllByOwnerId(1L, pageable)).thenReturn(pagedResponse);
        final List<ItemResponseDto> itemDtoList = itemService.getItemsByUserId(1L, 1, 1);
        assertNotNull(itemDtoList);
        assertEquals(1, itemDtoList.size());
        ItemResponseDto itemDto = itemDtoList.get(0);
        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
        assertThrows(ObjectNotFoundException.class, () -> itemService.getItemsByUserId(2L, 1, 1));
    }

    @Order(6)
    @Test
    void removeItemByIdTest() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.ofNullable(item));
        itemService.removeItemById(item.getId());
        assertThrows(ObjectNotFoundException.class, () -> itemService.removeItemById(7L));
    }

    @Order(7)
    @Test
    void searchItemsTest() {
        List<Item> items = new ArrayList<>(Collections.singletonList(item));
        Page<Item> pagedResponse = new PageImpl<>(items);
        Mockito
                .when(itemRepository.search(anyString(), any(Pageable.class)))
                .thenReturn(pagedResponse);
        List<ItemResponseDto> itemDtoList = itemService.searchItems("item", 1, 1);
        assertNotNull(itemDtoList);
        assertEquals(1, itemDtoList.size());
        ItemResponseDto itemDto = itemDtoList.get(0);
        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
        assertEquals(0, itemService.searchItems("", 1, 1).size());
        assertThrows(ObjectNotFoundException.class, () -> itemService.getItemsByUserId(1L, 1, 1));
        assertNotNull(itemService.searchItems("test", 1, 1));
    }

    @Order(8)
    @Test
    void createCommentTest() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.findFirstByItemAndBookerAndEndIsBeforeAndStatusIs(any(Item.class),
                any(User.class), any(LocalDateTime.class), any(StatusBooking.class))).thenReturn(booking);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        CommentResponseDto comment1 = itemService.createComment(commentDto, 1L, 1L);
        assertEquals(commentDto.getId(), comment1.getId());
        assertEquals(commentDto.getText(), comment1.getText());
        assertEquals(user.getName(), comment1.getAuthorName());
        assertThrows(ObjectNotFoundException.class, () -> itemService.createComment(commentDto, 1L, 2L));
    }

    @Order(9)
    @Test
    void createCommentWithoutBookingTest() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.findFirstByItemAndBookerAndEndIsBeforeAndStatusIs(any(Item.class), any(User.class),
                any(LocalDateTime.class), any(StatusBooking.class))).thenReturn(null);
        assertThrows(BadRequestException.class, () -> itemService.createComment(commentDto, 1L, 1L));
    }
}