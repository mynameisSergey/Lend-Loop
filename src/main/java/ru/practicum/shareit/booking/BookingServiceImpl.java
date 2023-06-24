package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.WrongDataException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override//I&T
    public BookingDto createBooking(BookingDto booking, long userId) {
        if (booking.getStart().isBefore(LocalDateTime.now()))
            throw new WrongDataException("Wrong start date.");
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Wrong user id=" + userId));

        Item item = itemRepository.findById(booking.getItemId())
                .filter(i -> i.getOwner().getId() != userId)
                .orElseThrow(() -> new NotFoundException("Item with id=" + booking.getItemId() + " not found"));

        if (!item.isAvailable()
                || repository.isAvailableForBooking(booking.getItemId(), booking.getStart(), booking.getEnd())) {
            throw new WrongDataException("Item with id=" + booking.getItemId() + " not available");
        }
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new WrongDataException("Wrong end time");
        }

        Booking savedBooking = repository.save(BookingMapper.toBooking(booking, booker, item));

        return BookingMapper.toBookingDto(savedBooking);

    }

    @Override//I&T
    public void approval(long bookingId, long userId, boolean approval) {
        if (repository.existsById(bookingId) && repository.getById(bookingId).getStatus().equals(Status.WAITING)) {
            if (approval) {
                Booking booking = getBooking(bookingId);
                if (booking.getItem().getOwner().getId() == userId) {
                    booking.setStatus(Status.APPROVED);
                    repository.save(booking);
                } else throw new NotFoundException("Wrong User Id.");
            } else {
                Booking booking = getBooking(bookingId);
                if (booking.getItem().getOwner().getId() == userId) {
                    booking.setStatus(Status.REJECTED);
                    repository.save(booking);
                } else throw new WrongDataException("Wrong User Id.");
            }
        } else throw new WrongDataException("Wrong Booking Id.");

    }

    @Override//I&T
    public BookingDto getBookingDto(long bookingId) {
        return BookingMapper.toBookingDto(repository.findById(bookingId).get());
    }


    public Booking getBooking(long bookingId) {
        return repository.findById(bookingId).get();
    }

    @Override//I&T
    public List<Booking> showAllUserBookings(long userId, State state) {
        return showAll(state).stream()
                .filter(x -> x.getBooker().getId() == userId)
                .sorted(new BookingComparator())
                .collect(Collectors.toList());
    }

    @Override//I&T
    public List<Booking> showOwnerBookings(long userId, State state) {
        List<Booking> listOfBooking = showAll(state).stream()
                .filter(x -> x.getItem().getOwner()
                        .getId() == userId).collect(Collectors.toList());
        Collections.reverse(listOfBooking);
        return listOfBooking;
    }

    @Override//I&T
    public List<Booking> showAll(State state) {
        switch (state) {
            case ALL:
                return repository.findAll().stream()
                        .sorted(Comparator.comparing(Booking::getStart))
                        .collect(Collectors.toList());
            case PAST:
                return repository.findAll().stream()
                        .filter(x -> x.getEnd().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case FUTURE:
                return repository.findAll().stream()
                        .filter(x -> x.getStart().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case CURRENT:
                return repository.findAll().stream()
                        .filter(x -> x.getStart().isBefore(LocalDateTime.now()))
                        .filter(x -> x.getEnd().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case WAITING:
                return repository.findAll().stream()
                        .filter(x -> x.getStatus().equals(Status.WAITING))
                        .collect(Collectors.toList());
            default:
                return repository.findAll().stream()
                        .filter(x -> x.getStatus().equals(Status.REJECTED))
                        .collect(Collectors.toList());
        }

    }

    @Override//I&T
    public List<Booking> showAll(State state, int firstPage, int size) {
        System.out.println(state + " " + firstPage + " " + size);
        System.out.println("showAll:" + repository.findAll());
        Pageable uPage = PageRequest.of(firstPage, size, Sort.by("id"));
        switch (state) {
            case ALL:
                //System.out.println("BINGO");
                // System.out.println(repository.findAll(PageRequest.of(firstPage, size, Sort.by("id").descending())).stream().collect(Collectors.toList()));
                return repository.findAll(PageRequest.of(firstPage, size, Sort.by("id").descending())).stream().collect(Collectors.toList());
            case PAST:
                return repository.findAll(uPage).stream()
                        .filter(x -> x.getEnd().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case FUTURE:
                return repository.findAll(uPage).stream()
                        .filter(x -> x.getStart().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case CURRENT:
                return repository.findAll(uPage).stream()
                        .filter(x -> x.getStart().isBefore(LocalDateTime.now()))
                        .filter(x -> x.getEnd().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case WAITING:
                return repository.findAll(uPage).stream()
                        .filter(x -> x.getStatus().equals(Status.WAITING))
                        .collect(Collectors.toList());
            default:
                return repository.findAll(uPage).stream()
                        .filter(x -> x.getStatus().equals(Status.REJECTED))
                        .collect(Collectors.toList());
        }

    }

    @Override//I&T
    public boolean isBookingExist(long bookingId) {
        return repository.existsById(bookingId);
    }

    @Override//I&T
    public List<Booking> getOwnerBookings(int firstPage, int size, long userId) {
        if (firstPage < 0 || size < 0) {
            throw new WrongDataException("");
        }
        if (size == 0) {
            throw new WrongDataException("");
        }
        return repository.getOwnerBookings(userId, PageRequest.of(firstPage, size, Sort.by("start").descending()));
    }


}