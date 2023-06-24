package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {"db.name=booking_test"})
@AutoConfigureMockMvc
public class BookingControllerTest {
    @MockBean
    private BookingService bookingService;

    @MockBean
    private UserService userService;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetBookings() throws Exception {
        // given
        String state = "ALL";
        long userId = 1L;
        int from = 0;
        int size = 20;
        when(userService.getUser(anyLong())).thenReturn(java.util.Optional.of(new User()));
        when(bookingService.showAllUserBookings(userId, State.PAST)).thenReturn(Collections.emptyList());

        // when + then
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .param("state", state))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(bookingService, times(1)).showAll(State.ALL, from, size);
    }

    @Test
    void testGetOwnerBookings() throws Exception {
        // given
        String state = "ALL";
        long userId = 1L;
        int from = 0;
        int size = 20;
        when(userService.getUser(anyLong())).thenReturn(java.util.Optional.of(new User()));
        when(bookingService.showAllUserBookings(userId, State.PAST)).thenReturn(Collections.emptyList());

        // when + then
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .param("state", state))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(bookingService, times(1)).getOwnerBookings(from, size, userId);
    }

    @Test
    void testBookItem() throws Exception {
        // given
        long userId = 1L;
        long itemId = 2L;
        BookingDto bookingDto = new BookingDto(3L, itemId, userId, null, null);
        when(bookingService.createBooking(any(), anyLong())).thenReturn(bookingDto);

        // when + then
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .content(String.format("{\"itemId\": %s,\"start\": \"%s\", \"end\":\"%s\"}", itemId, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()));
        verify(bookingService, times(1)).createBooking(any(), anyLong());
    }

    @Test
    void testGetBooking() throws Exception {
        // given
        long userId = 1L;
        long bookerId = 0L;
        BookingDto bookingDto = new BookingDto(3L, userId, null, null);
        when(userService.getUser(anyLong())).thenReturn(java.util.Optional.of(new User()));
        when(bookingService.isBookingExist(anyLong())).thenReturn(true);
        when(bookingService.getBookingDto(bookingDto.getId())).thenReturn(bookingDto);

        // when + then
        mockMvc.perform(get("/bookings/" + bookingDto.getId())
                        .header("X-Sharer-User-Id", bookerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3));
        verify(bookingService, times(2)).getBookingDto(anyLong());
    }

}