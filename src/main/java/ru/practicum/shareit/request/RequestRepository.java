package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.ItemRequest;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {
}