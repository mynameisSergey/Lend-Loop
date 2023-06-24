package ru.practicum.shareit.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
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
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
@TestPropertySource(properties = {"db.name=request_test"})
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class ItemRequestControllerTest {
    @MockBean
    private RequestService requestService;

    @MockBean
    private UserService userService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testAddRequest() throws Exception {
        long userId = 1L;
        when(requestService.addRequest(anyLong(), any())).thenReturn(new ItemRequest());
        when(userService.getUser(anyLong())).thenReturn(Optional.of(new User()));
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(new ItemRequest()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(0));
        verify(requestService, times(1)).addRequest(anyLong(), any());
    }

    @Test
    void getUserRequests() throws Exception {
        long userId = 1L;
        int from = 0;
        int size = 20;
        when(requestService.getUserRequests(anyLong())).thenReturn(new LinkedList<>());
        when(userService.getUser(anyLong())).thenReturn(Optional.of(new User()));
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(new ItemRequest()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(requestService, times(1)).getUserRequests(userId);
    }

    @Test
    void showRequests() throws Exception {
        long userId = 1L;
        int from = 0;
        int size = 20;
        when(requestService.showRequests(anyLong())).thenReturn(new LinkedList<>());
        when(userService.getUser(anyLong())).thenReturn(Optional.of(new User()));
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(new ItemRequest()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(requestService, times(1)).showRequests(userId);
    }

    @Test
    void showRequestsPageable() throws Exception {
        long userId = 1L;
        int from = 0;
        int size = 20;
        when(requestService.showRequests(anyLong())).thenReturn(new LinkedList<>());
        when(userService.getUser(anyLong())).thenReturn(Optional.of(new User()));
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .content(mapper.writeValueAsString(new ItemRequest()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(requestService, times(1)).showRequests(from, size, userId);
    }

    @Test
    void getRequest() throws Exception {
        long userId = 1L;
        when(requestService.getRequestById(anyLong())).thenReturn(new ItemRequest());
        when(userService.getUser(anyLong())).thenReturn(Optional.of(new User()));
        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(new ItemRequest()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(0));
        verify(requestService, times(1)).getRequestById(anyLong());

    }

}