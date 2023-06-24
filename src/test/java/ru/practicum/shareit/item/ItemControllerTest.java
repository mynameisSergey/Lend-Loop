package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ItemServiceImpl itemService;
    private UserRepository userRepository;
    private User user;
    private ItemDto itemDto;
    private ItemDto itemDto1;
    private ItemResponseDto itemResponseDto;
    private ItemResponseDto itemResponseDto1;
    private CommentDto commentDto;

    @BeforeEach
    void beforeEach() {
        userRepository = mock(UserRepository.class);
        user = new User(1L, "user", "user@email.ru");
        itemDto = new ItemDto(1L, "item", "item test", true, 1L);
        itemDto1 = new ItemDto(1L, "item", "", true, 1L);
        itemResponseDto = new ItemResponseDto(1L, "item", "item test", true, null, null, null, 1L);
        itemResponseDto1 = new ItemResponseDto(1L, "item", "", true, null, null, null, 1L);
        commentDto = new CommentDto(1L, "Comment for item");
    }

    @Test
    void createItemTest() throws Exception {
        when(userRepository.save(user)).thenReturn(user);
        userRepository.save(user);
        when(itemService.createItem(itemDto, 1L)).thenReturn(itemResponseDto);
        String body = mapper.writeValueAsString(itemDto);
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void createItemFailDescriptionTest() throws Exception {
        when(userRepository.save(user)).thenReturn(user);
        userRepository.save(user);
        when(itemService.createItem(itemDto1, 1L)).thenReturn(itemResponseDto1);
        String body = mapper.writeValueAsString(itemDto1);
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void updateItemTest() throws Exception {
        when(userRepository.save(user)).thenReturn(user);
        when(itemService.updateItem(any(), eq(1L), eq(1L))).thenReturn(itemResponseDto);
        String body = mapper.writeValueAsString(itemDto);
        mockMvc.perform(patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class));
    }

    @Test
    void getItemByIdTest() throws Exception {
        when(itemService.createItem(itemDto, 1L)).thenReturn(itemResponseDto);
        when(itemService.getItemById(1L, 1L)).thenReturn(itemResponseDto);
        mockMvc.perform(get("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class));
    }

    @Test
    void getItemsByUserIdTest() throws Exception {
        when(itemService.getItemsByUserId(1L, 1, 1)).thenReturn(Collections.singletonList(itemResponseDto));
        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "1")
                        .param("size", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void removeItemByIdTest() throws Exception {
        mockMvc.perform(delete("/items/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createCommentTest() throws Exception {
        String body = mapper.writeValueAsString(commentDto);
        mockMvc.perform(post("/items/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void searchItemsTest() throws Exception {
        when(itemService.searchItems("item", 1, 1)).thenReturn(Collections.singletonList(itemResponseDto));
        mockMvc.perform(get("/items/search?text=test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk());
    }
}