package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(Long ownerId, Pageable pageable);

    List<Booking> findAllByItemOwner(User owner, Pageable pageable);

    @Query(
            " select i from Booking i " +
                    "where i.item.id = :ownerId"
    )
    List<Booking> getOwnerBookings(@Param("ownerId") Long ownerId, Pageable pageable);

    @Query(" select (count(b) > 0) " +
            "from Booking b " +
            "where b.item.id = ?1 " +
            "  and b.status = 'APPROVED' " +
            "  and b.start <= ?2 " +
            "  and b.end >= ?3")
    boolean isAvailableForBooking(Long itemId, LocalDateTime start, LocalDateTime end);
}