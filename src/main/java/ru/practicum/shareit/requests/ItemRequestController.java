package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * // TODO .
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {

    private final RequestService service;
    private final UserService userService;

    @PostMapping
    public @Valid ItemRequest addRequest(@RequestHeader("X-Sharer-User-Id") String ownerId, @Valid @RequestBody ItemRequest request) {
        System.out.println("addRequest");
        System.out.println(request);
        if (userService.getUser(Long.parseLong(ownerId)).isPresent()) {
            request.setRequestor(userService.getUser(Long.parseLong(ownerId)).get());
            return service.addRequest(Long.parseLong(ownerId), request);
        }
        throw new ValidationException("");
    }

    @GetMapping
    public @Valid List<ItemRequest> getUserRequests(@RequestHeader("X-Sharer-User-Id") String ownerId) {
        System.out.println("getUserRequests");
        if (userService.getUser(Long.parseLong(ownerId)).isPresent()) {
            return service.getUserRequests(Long.parseLong(ownerId));
        }
        throw new ValidationException("");
    }

    @GetMapping("/all")
    public @Valid List<ItemRequest> showRequests(@RequestHeader("X-Sharer-User-Id") String ownerId,
                                                 @RequestParam(name = "from", defaultValue = "") String from,
                                                 @RequestParam(name = "size", defaultValue = "") String size) {
        if (userService.getUser(Long.parseLong(ownerId)).isPresent()) {
            if (from.equals("") || size.equals("")) {
                return service.showRequests(Long.parseLong(ownerId));
            } else {
                return service.showRequests(Integer.parseInt(from), Integer.parseInt(size), Long.parseLong(ownerId));
            }
        }
        throw new ValidationException("");
    }

    @GetMapping("/{requestId}")
    public @Valid ItemRequest getRequest(@RequestHeader("X-Sharer-User-Id") String ownerId, @PathVariable("requestId") long id) {
        System.out.println("getRequest");
        if (userService.getUser(Long.parseLong(ownerId)).isPresent()) {
            return service.getRequestById(id);
        }
        throw new ValidationException("");
    }

}