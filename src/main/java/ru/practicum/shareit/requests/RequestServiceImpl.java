package ru.practicum.shareit.requests;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.WrongDataException;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Getter
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;

    @Override//I&T
    public ItemRequest addRequest(long userId, ItemRequest request) {
        if (request.getDescription() == null) throw new WrongDataException("");
        repository.save(request);
        return repository.getById(request.getId());
    }

    @Override//I&T
    public List<ItemRequest> getUserRequests(long userId) {
        return repository.findAll().stream().filter(x -> x.getRequestor().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override//I&T
    public List<ItemRequest> showRequests(int firstPage, int size, long userId) {
        if (firstPage < 0 || size < 0) {
            throw new WrongDataException("");
        }
        if (size == 0) {
            throw new WrongDataException("");
        }
        Pageable uPage = PageRequest.of(firstPage, size, Sort.by("created"));
        return repository.findNotUserRequests(userId, uPage);
    }

    @Override//I&T
    public List<ItemRequest> showRequests(long userId) {
        return repository.findAll().stream().filter(x -> x.getRequestor().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override//I&T
    public ItemRequest getRequestById(long requestId) {
        if (repository.existsById(requestId)) {
            return repository.getById(requestId);
        }
        throw new NotFoundException("Request with id is not exist.");
    }

    @Override//I&T
    public List<ItemRequest> getAllRequests(long ownerId) {
        return repository.findAll().stream().filter(x -> x.getRequestor().getId() != ownerId)
                .collect(Collectors.toList());
    }
}