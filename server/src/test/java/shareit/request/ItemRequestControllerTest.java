package shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;


import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService requestService;

    private final User user = User.builder()
            .id(1L)
            .name("username")
            .email("email@email.com")
            .build();

    private final ItemRequestDto requestDto = ItemRequestDto.builder()
            .id(1L)
            .description("description")
            .created(LocalDateTime.now())
            .items(List.of())
            .build();

    @Test
    @SneakyThrows
    @DisplayName("Тестирование эндпоинта post /requests")
    void createRequest() {
        when(requestService.add(any(), any())).thenReturn(requestDto);

        String result = mockMvc.perform(MockMvcRequestBuilders.post("/requests")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(requestDto), result);
    }

    @Test
    @SneakyThrows
    @DisplayName("Тестирование эндпоинта get /requests")
    void getUserRequests() {
        when(requestService.getUserRequests(user.getId())).thenReturn(List.of(requestDto));

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(requestDto)), result);
    }

    @Test
    @SneakyThrows
    @DisplayName("Тестирование эндпоинта get /requests/all")
    void getAllRequests() {
        Integer from = 0;
        Integer size = 10;
        when(requestService.getAllRequests(user.getId(), from, size)).thenReturn(List.of(requestDto));

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(requestDto)), result);
    }

    @Test
    @SneakyThrows
    @DisplayName("Тестирование эндпоинта get /requests/{requestId}")
    void get() {
        Long requestId = 1L;

        when(requestService.getRequestById(user.getId(), requestId)).thenReturn(requestDto);

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/requests/{requestId}", requestId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(requestDto), result);
    }

}
