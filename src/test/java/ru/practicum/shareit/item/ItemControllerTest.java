package ru.practicum.shareit.item;

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
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ItemController.class)
@TestPropertySource(properties = {"db.name=item_test"})
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class ItemControllerTest {
    @MockBean
    private ItemService itemService;
    @MockBean
    private ItemMapper itemMapper;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void create() throws Exception {
        ItemDto itemDto = new ItemDto(0, "1", "1", true);
        long userId = 1L;
        when(itemService.addItem(any(), anyLong())).thenReturn(new ItemDto());
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(0)));
    }


    @Test
    void show() throws Exception {
        ItemDto itemDto = new ItemDto(0, "1", "1", true);
        long userId = 1L;
        when(itemService.show(anyLong())).thenReturn(new LinkedList<>());
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", "1");
        mockMvc.perform(get("/items")
                        .headers(headers))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void showPageable() throws Exception {
        User user = new User(1, "1", "1@mail.ru");
        Item item = new Item(1, "1", "1", true, user);
        ItemDto itemDto = new ItemDto(0, "1", "1", true);
        List<ItemDto> items = new LinkedList<>();
        items.add(itemDto);
        when(itemService.show(anyLong(), anyInt(), anyInt())).thenReturn(new PageImpl<>(items));
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", "1");
        mockMvc.perform(get("/items")
                        .headers(headers)
                        .param("from", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(0)))
                .andExpect(jsonPath("$[0].name", is("1")))
                .andExpect(jsonPath("$[0].description", is("1")));
    }

    @Test
    void update() throws Exception {
        ItemDto itemDto = new ItemDto(0, "1", "1", true);
        when(itemService.updateItem(itemDto, 1L, 1)).thenReturn(new ItemDto());
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", "1");
        mockMvc.perform(patch("/items/1")
                        .headers(headers)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(0L), Long.class));
    }

    @Test
    void getItemDtoById() throws Exception {
        ItemDto itemDto = new ItemDto(0, "1", "1", true);
        when(itemService.getItemDtoById(1, 1L)).thenReturn(itemDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", "1");
        mockMvc.perform(get("/items/1")
                        .headers(headers)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(0L), Long.class))
                .andExpect(jsonPath("$.name", is("1")))
                .andExpect(jsonPath("$.description", is("1")));
    }

    @Test
    void search() throws Exception {
        ItemDto itemDto = new ItemDto(0, "1", "1", true);
        List<ItemDto> items = new LinkedList<>();
        items.add(itemDto);
        when(itemService.searchItems("item")).thenReturn(items);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", "1");
        mockMvc.perform(get("/items/search?text=item")
                        .headers(headers)
                        .content(mapper.writeValueAsString("item"))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(0L), Long.class))
                .andExpect(jsonPath("$[0].name", is("1")))
                .andExpect(jsonPath("$[0].description", is("1")));
    }

    @Test
    void searchPageable() throws Exception {
        ItemDto itemDto = new ItemDto(0, "1", "1", true);
        List<ItemDto> items = new LinkedList<>();
        items.add(itemDto);
        when(itemService.searchItems("item", 0, 1)).thenReturn(items);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", "1");
        mockMvc.perform(get("/items/search?text=item")
                        .headers(headers)
                        .param("from", "0")
                        .param("size", "1")
                        .content(mapper.writeValueAsString("item"))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(0L), Long.class))
                .andExpect(jsonPath("$[0].name", is("1")))
                .andExpect(jsonPath("$[0].description", is("1")));
    }

    @Test
    void addComment() throws Exception {
        CommentDto commentDto = new CommentDto(1, "text", 1, "Karl");
        when(itemService.addComment(commentDto, 1, 1L))
                .thenReturn(commentDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sharer-User-Id", "1");
        mockMvc.perform(post("/items/1/comment")
                        .headers(headers)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.text", is("text")));
    }
}