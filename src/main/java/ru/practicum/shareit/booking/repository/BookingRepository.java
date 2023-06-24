package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findFirstByItemAndBookerAndEndIsBefore(Item item, User user, LocalDateTime now);

    Booking findFirstByItemAndStartIsBeforeOrderByEndDesc(Item item, LocalDateTime now);

    Booking findFirstByItemAndStartIsAfterAndStatusIsNotOrderByStartAsc(Item item, LocalDateTime now, StatusBooking status);

    List<Booking> findBookingsByBooker(User booker);

    List<Booking> findAllByBookerAndStartIsBeforeAndEndIsAfter(User booker, LocalDateTime dateTime, LocalDateTime dateTime1);

    List<Booking> findAllByBookerAndEndIsBeforeAndStatusIs(User booker, LocalDateTime dateTime, StatusBooking status);

    List<Booking> findAllByBookerAndStartIsAfter(User booker, LocalDateTime dateTime);

    List<Booking> findAllByBookerAndStatusIs(User booker, StatusBooking status);

    List<Booking> findAllByItemOwnerId(Long ownerId);

    List<Booking> findAllByItemOwnerAndStartIsBeforeAndEndIsAfter(User owner, LocalDateTime dateTime, LocalDateTime dateTime1);

    List<Booking> findAllByItemOwnerIdAndEndIsBeforeAndStatusIs(Long ownerId, LocalDateTime dateTime, StatusBooking status);

    List<Booking> findAllByItemOwnerIdAndStartIsAfter(Long ownerId, LocalDateTime dateTime);

    List<Booking> findAllByItemOwnerIdAndStatusIs(Long ownerId, StatusBooking status);
}