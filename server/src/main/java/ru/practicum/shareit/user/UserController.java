package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> add(@Valid @RequestBody UserDto user) {
        log.info("POST запрос на создание пользователя: {}", user);
        return ResponseEntity.ok(userService.add(user));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> update(@RequestBody UserDto userDto, @PathVariable Long userId) {
        log.info("PATCH запрос на обновление пользователя c id: {}", userId);
        return ResponseEntity.ok(userService.update(userId, userDto));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        log.info("GET запрос на получение списка всех пользователей.");
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> get(@PathVariable Long userId) {
        log.info("GET запрос на получение пользователя c id: {}", userId);
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        log.info("DELETE запрос на удаление пользователя с id: {}", userId);
        userService.delete(userId);
    }
}