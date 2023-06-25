package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequesterId(Long userId);
    List<ItemRequest> findAllByRequester_IdNotOrderByCreatedDesc(Long userId, Pageable pageable);
}