package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ItemRequestService itemRequestService;

    @Test
    void createRequestTest() throws Exception {
        User user = new User(1L, "User1", "user@email.ru");
        String body = mapper.writeValueAsString(new ItemRequestDto(1L, "item", LocalDateTime.now()));
        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1")
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void getRequestTest() throws Exception {
        mockMvc.perform(get("/requests/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void getUserRequestsTest() throws Exception {
        mockMvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllRequestTest() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("from", "1")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk());
    }
}
