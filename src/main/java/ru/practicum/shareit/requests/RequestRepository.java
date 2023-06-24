package ru.practicum.shareit.requests;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {

    Page<ItemRequest> findAllByRequesterId(Long ownerId, Pageable pageable);

    Page<ItemRequest> findAllByRequesterIdIsNot(Long ownerId, Pageable pageable);
}