package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(" select i from Item  i " +
            "where upper(i.description) like upper(concat('%', :text, '%')) and i.available=true")
    List<Item> search(String text);

    List<Item> findAllByOwnerId(Long ownerId);
}