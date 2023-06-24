package ru.practicum.shareit.requests;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {
    @Query(
            " select i from ItemRequest i " +
                    "where i.requestor.id!= :ownerId"
    )
    List<ItemRequest> findNotUserRequests(@Param("ownerId") Long ownerId, Pageable pageable);
}