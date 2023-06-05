package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.IllegalStateException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public BookingDto createBooking(BookingDto bookingDto, Long userId) {
        Booking newBooking = new Booking();
        BookingMapper.toBooking(newBooking, bookingDto);
        if (bookingDto.getStart().isBefore(LocalDateTime.now()) || !(bookingDto.getStart().isBefore(bookingDto.getEnd()))) {
            throw new BadRequestException("Некорректные даты бронирования");
        }
        var booker = userRepository.findById(userId);
        if (booker.isEmpty()) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        newBooking.setBooker(booker.get());
        var item = existItemById(bookingDto.getItemId());
        if (booker.get().equals(item.get().getOwner())) {
            throw new ObjectNotFoundException("Вещь другого собственника:" + item.get().getOwner().getId());
        }
        newBooking.setItem(item.get());
        newBooking.setStatus(StatusBooking.WAITING);
        Booking createdBooking = bookingRepository.save(newBooking);
        return BookingMapper.toBookingDto(createdBooking);
    }

    @Override
    public BookingDto getBooking(Long id, Long userId) {
        var booking = bookingRepository.findById(id);
        if (booking.isEmpty()) {
            throw new ObjectNotFoundException("Бронирование не найдено");
        } else {
            return BookingMapper.toBookingDto(booking.get());
        }
    }


    @Override
    @Transactional
    public BookingDto updateBooking(Long bookingId, Long userId, Boolean approved) {
        Booking updatedBooking;
        Booking booking = new Booking(bookingRepository.getReferenceById(bookingId));
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new ObjectNotFoundException("Вещь другого собственника:" + userId);
        }
        if ((booking.getStatus() == StatusBooking.APPROVED && approved)
                || (booking.getStatus() == StatusBooking.REJECTED && !approved)) {
            throw new IllegalStateException("Некорректный статус бронирования");
        }
        if (approved) {
            booking.setStatus(StatusBooking.APPROVED);
        } else {
            booking.setStatus(StatusBooking.REJECTED);
        }
        updatedBooking = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(updatedBooking);
    }

    @Override
    public List<BookingDto> getAllBookingsByState(Long userId, String stringState) {
        var booker = userRepository.findById(userId);
        if (booker.isEmpty()) {
            throw new ObjectNotFoundException("Пользователь не найден: " + userId);
        }
        State state = State.checkState(stringState).orElseThrow(() -> new IllegalStateException("Unknown state: " + stringState));
        return stateToRepository(booker.get(), state)
                .stream()
                .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getAllBookingsByStateAndOwner(Long userId, String stringState) {
        var booker = userRepository.findById(userId);
        if (booker.isEmpty()) {
            throw new ObjectNotFoundException("Пользователь не найден: " + userId);
        }
        State state = State.checkState(stringState).orElseThrow(() -> new IllegalStateException("Unknown state: " + stringState));
        return stateToRepositoryAndOwner(booker.get(), state)
                .stream()
                .filter(b -> Objects.equals(b.getItem().getOwner().getId(), userId))
                .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                .collect(Collectors.toList());
    }

    private List<BookingDto> stateToRepositoryAndOwner(User owner, State state) {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> result = new ArrayList<>();
        switch (state) {
            case ALL:
                result = bookingRepository.findAllByItemOwnerId(owner.getId());
                break;
            case CURRENT:
                result = bookingRepository.findAllByItemOwnerAndStartIsBeforeAndEndIsAfter(owner, now, now);
                break;
            case PAST:
                result = bookingRepository.findAllByItemOwnerIdAndEndIsBeforeAndStatusIs(owner.getId(), now, StatusBooking.APPROVED);
                break;
            case FUTURE:
                result = bookingRepository.findAllByItemOwnerIdAndStartIsAfter(owner.getId(), now);
                break;
            case WAITING:
                result = bookingRepository.findAllByItemOwnerIdAndStatusIs(owner.getId(), StatusBooking.WAITING);
                break;
            case REJECTED:
                result = bookingRepository.findAllByItemOwnerIdAndStatusIs(owner.getId(), StatusBooking.REJECTED);
                break;
        }
        return result.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private List<BookingDto> stateToRepository(User owner, State state) {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> result = new ArrayList<>();
        switch (state) {
            case ALL:
                result = bookingRepository.findBookingsByBooker(owner);
                break;
            case CURRENT:
                result = bookingRepository.findAllByBookerAndStartIsBeforeAndEndIsAfter(owner, now, now);
                break;
            case PAST:
                result = bookingRepository.findAllByBookerAndEndIsBeforeAndStatusIs(owner, now, StatusBooking.APPROVED);
                break;
            case FUTURE:
                result = bookingRepository.findAllByBookerAndStartIsAfter(owner, now);
                break;
            case WAITING:
                result = bookingRepository.findAllByBookerAndStatusIs(owner, StatusBooking.WAITING);
                break;
            case REJECTED:
                result = bookingRepository.findAllByBookerAndStatusIs(owner, StatusBooking.REJECTED);
                break;
        }
        return result.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private Optional<Item> existItemById(Long id) {
        var item = itemRepository.findById(id);
        if (item.isEmpty()) {
            throw new ObjectNotFoundException("Вещь не найдена");
        }
        if (!item.get().getAvailable()) {
            throw new BadRequestException("Вещь недоступна для бронирования");
        }
        return item;
    }
}