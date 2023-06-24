package ru.practicum.shareit.requests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ItemRequestServiceTest {

    @Mock
    private RequestRepository itemRequestRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    private ItemRequestServiceImpl itemRequestService;
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;
    private UserDto userDto;
    private User user;
    private User user1;

    @BeforeEach
    void beforeEach() {
        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, userRepository, itemRepository);
        userDto = new UserDto(1L, "User1", "user@email.ru");
        user = new User(1L, "User1", "user@email.ru");
        user1 = new User(2L, "User2", "user2@email.ru");
        itemRequest = new ItemRequest(1L, "Request", user, LocalDateTime.now());
        itemRequestDto = new ItemRequestDto(1L, "Request", LocalDateTime.now());
    }

    @Order(1)
    @Test
    void createRequestTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(any())).thenReturn(itemRequest);
        ItemRequestResponseDto itemRequestDb = itemRequestService.createRequest(itemRequestDto, 1L);
        assertEquals(itemRequestDto.getId(), itemRequestDb.getId());
        assertEquals(itemRequestDto.getDescription(), itemRequestDb.getDescription());
        assertEquals(LocalDateTime.now().format((DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss"))),
                itemRequestDb.getCreated().format((DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss"))));
    }

    @Order(2)
    @Test
    void getRequestTest() {
        when(itemRequestRepository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        ItemRequestResponseDto itemRequestDb = itemRequestService.getRequest(itemRequest.getId(), userDto.getId());
        assertEquals(itemRequest.getId(), itemRequestDb.getId());
        assertEquals(itemRequest.getDescription(), itemRequestDb.getDescription());
        assertEquals(itemRequest.getCreated().format(DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")),
                itemRequestDb.getCreated().format(DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")));
    }

    @Order(3)
    @Test
    void getUserRequests() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        List<ItemRequest> itemRequests = new ArrayList<>(Collections.singletonList(itemRequest));
        Page<ItemRequest> pagedResponse = new PageImpl<>(itemRequests);
        when(itemRequestRepository.findAllByRequesterId(anyLong(), any(Pageable.class))).thenReturn(pagedResponse);
        List<ItemRequestResponseDto> itemRequestList = itemRequestService.getUserRequests(userDto.getId(), 1, 1);
        assertNotNull(itemRequestList);
        assertEquals(1, itemRequestList.size());
        ItemRequestResponseDto itemRequestDb = itemRequestList.get(0);
        assertEquals(itemRequest.getId(), itemRequestDb.getId());
        assertEquals(itemRequest.getDescription(), itemRequestDb.getDescription());
        assertEquals(itemRequest.getCreated().format(DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")),
                itemRequestDb.getCreated().format(DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")));
        assertNotNull(itemRequestDb.getItems());
    }

    @Order(4)
    @Test
    void getAllRequestTest() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        List<ItemRequest> itemRequests = new ArrayList<>(Collections.singletonList(itemRequest));
        Page<ItemRequest> pagedResponse = new PageImpl<>(itemRequests);
        when(itemRequestRepository.findAllByRequesterIdIsNot(anyLong(), any(Pageable.class))).thenReturn(pagedResponse);
        List<ItemRequestResponseDto> itemRequestList = itemRequestService.getAllRequests(2L, 1, 1);
        assertEquals(1, itemRequestList.size());
        ItemRequestResponseDto itemRequestDb = itemRequestList.get(0);
        assertEquals(itemRequest.getId(), itemRequestDb.getId());
        assertEquals(itemRequest.getDescription(), itemRequestDb.getDescription());
        assertEquals(itemRequest.getCreated().format(DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")),
                itemRequestDb.getCreated().format(DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")));
        assertThrows(ObjectNotFoundException.class, () -> itemRequestService.getAllRequests(4L, 1, 1));
    }
}