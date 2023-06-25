package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select i from Item  i " +
            "where upper(i.description) like upper(concat('%', :text, '%')) and i.available=true")
    Page<Item> search(String text, Pageable pageable);

    Page<Item> findAllByOwnerId(Long ownerId, Pageable pageable);

    List<Item> findAllByRequestId(Long requestId);

    List<Item> findAllByRequestIn(List<ItemRequest> requests);
}