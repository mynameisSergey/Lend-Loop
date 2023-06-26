package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    final Integer FROM = 0;

    final Integer SIZE = 10;

    final String SHARER = "X-Sharer-User-Id";

    private final User user = User.builder()
            .id(1L)
            .name("username")
            .email("email@email.com")
            .build();

    private final Item item = Item.builder()
            .id(1L)
            .name("item name")
            .description("description")
            .owner(user)
            .build();

    private final BookingDto bookingDto = BookingDto.builder()
            .itemId(1L)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusDays(1L))
            .build();

    private final BookingDtoOut bookingDtoOut = BookingDtoOut.builder()
            .id(1L)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now())
            .status(BookingStatus.WAITING)
            .booker(user)
            .item(item)
            .build();

    @Test
    @SneakyThrows
    @DisplayName("Тестирование эндпоинта post /bookings")
    void createBooking_whenBookingIsValid_thenReturnStatusOkTest() {
        when(bookingService.add(user.getId(), bookingDto)).thenReturn(bookingDtoOut);

        String result = mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .header(SHARER, user.getId())
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDtoOut), result);
    }

    @Test
    @SneakyThrows
    @DisplayName("Тестирование эндпоинта post /bookings с невалидными данными")
    void createBooking_whenBookingIsNotValid_thenReturnBadRequestTest() {
        bookingDto.setItemId(null);
        bookingDto.setStart(null);
        bookingDto.setEnd(null);

        when(bookingService.add(user.getId(), bookingDto)).thenReturn(bookingDtoOut);

        mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .header(SHARER, user.getId())
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isBadRequest());

        verify(bookingService, never()).add(user.getId(), bookingDto);
    }

    @Test
    @SneakyThrows
    @DisplayName("Тестирование эндпоинта patch /bookings/{bookingId}")
    void update_whenBookingIsValid_thenReturnStatusIsOkTest() {
        Boolean approved = true;
        Long bookingId = 1L;

        when(bookingService.update(user.getId(), bookingId, approved)).thenReturn(bookingDtoOut);

        String result = mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .contentType("application/json")
                        .header(SHARER, user.getId())
                        .param("approved", String.valueOf(approved)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDtoOut), result);
    }

    @Test
    @SneakyThrows
    @DisplayName("Тестирование эндпоинта get /bookings/{bookingId}")
    void getById_whenBookingIsValid_thenReturnStatusIsOkTest() {
        Long bookingId = 1L;

        when(bookingService.getBookingById(user.getId(), bookingId)).thenReturn(bookingDtoOut);

        String result = mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header(SHARER, user.getId())).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDtoOut), result);
    }

    @Test
    @SneakyThrows
    @DisplayName("Тестирование эндпоинта get /bookings")
    void getAll_thenReturnStatusIsOkTest() {
       String state = "ALL";

        when(bookingService.getAll(user.getId(), BookingState.ALL.toString(), 0, 10))
                .thenReturn(List.of(bookingDtoOut));

        String result = mockMvc.perform(get("/bookings")
                        .param("state", state)
                        .param("from", String.valueOf(FROM))
                        .param("size", String.valueOf(SIZE))
                        .header(SHARER, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(bookingDtoOut)), result);
    }

    @Test
    @SneakyThrows
    @DisplayName("Тестирование эндпоинта get /bookings с некорректным статусом")
    void getAll_whenBookingStatusIsInvalid_thenThrowIllegalArgumentExceptionTest() {
        String state = "ERROR";

        when(bookingService.getAll(user.getId(), BookingState.ALL.toString(), 0, 10))
                .thenReturn(List.of(bookingDtoOut));

        mockMvc.perform(get("/bookings")
                        .param("state", state)
                        .param("from", String.valueOf(FROM))
                        .param("size", String.valueOf(SIZE))
                        .header(SHARER, user.getId()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @SneakyThrows
    @DisplayName("Тестирование эндпоинта get /bookings/owner")
    void getAllByOwnersTest() {

        String state = "ALL";

        when(bookingService.getAllOwners(user.getId(), BookingState.ALL.toString(), 0, 10))
                .thenReturn(List.of(bookingDtoOut));

        String result = mockMvc.perform(get("/bookings/owner")
                        .param("state", state)
                        .param("from", String.valueOf(FROM))
                        .param("size", String.valueOf(SIZE))
                        .header(SHARER, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(bookingDtoOut)), result);
    }

    @Test
    @SneakyThrows
    @DisplayName("Тестирование эндпоинта get /bookings/owner")
    void getAllByOwner_whenBookingStatusIsNotValid_thenThrowIllegalArgumentExceptionTest() {
             String state = "ERROR";

        when(bookingService.getAllOwners(user.getId(), BookingState.ALL.toString(), 0, 10))
                .thenReturn(List.of(bookingDtoOut));

        mockMvc.perform(get("/bookings/owner")
                        .param("state", state)
                        .param("from", String.valueOf(FROM))
                        .param("size", String.valueOf(SIZE))
                        .header(SHARER, user.getId()))
                .andExpect(status().isInternalServerError());
    }
}
