package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.IllegalStateException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    public BookingResponseDto createBooking(BookingDto bookingDto, Long userId) {
        Booking newBooking = new Booking();
        BookingMapper.toBooking(newBooking, bookingDto);
        if (!(bookingDto.getStart().isBefore(bookingDto.getEnd()))) {
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
        return BookingMapper.toBookingResponseDto(createdBooking);
    }

    @Override
    public BookingResponseDto getBooking(Long id, Long userId) {
        var booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Бронирование не найдено"));
        if (!Objects.equals(booking.getBooker().getId(), userId) &&
                !Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new ObjectNotFoundException("Вещь другого собственника:" + userId);
        }
        return BookingMapper.toBookingResponseDto(booking);
    }

    @Override
    @Transactional
    public BookingResponseDto updateBooking(Long bookingId, Long userId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ObjectNotFoundException("Бронирование не найдено"));
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
        return BookingMapper.toBookingResponseDto(booking);
    }

    @Override
    public List<BookingResponseDto> getAllBookingsByState(Long userId, String stringState, Integer from, Integer size) {
        Pageable pageable;
        pageable = PageRequest.of(from / size, size, Sort.by("start").descending());
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден: " + userId));
        State state = checkState(stringState).orElseThrow(() -> new IllegalStateException("Unknown state: " + stringState));
        return new ArrayList<>(stateToRepository(booker, state, pageable));
    }

    @Override
    public List<BookingResponseDto> getAllBookingsByStateAndOwner(Long userId, String stringState, Integer from, Integer size) {
        Pageable pageable;
        pageable = PageRequest.of(from / size, size, Sort.by("start").descending());
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден: " + userId));
        State state = checkState(stringState).orElseThrow(() -> new IllegalStateException("Unknown state: " + stringState));
        return new ArrayList<>(stateToRepositoryAndOwner(booker, state, pageable));
    }

    public List<BookingResponseDto> stateToRepositoryAndOwner(User owner, State state, Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> resultList = new ArrayList<>();
        Page<Booking> result = new PageImpl<>(resultList);
        switch (state) {
            case ALL:
                result = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(owner.getId(), pageable);
                break;
            case CURRENT:
                result = bookingRepository.findAllByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(owner, now, now, pageable);
                break;
            case PAST:
                result = bookingRepository.findAllByItemOwnerIdAndEndIsBeforeAndStatusIsOrderByStartDesc(owner.getId(), now, StatusBooking.APPROVED, pageable);
                break;
            case FUTURE:
                result = bookingRepository.findAllByItemOwnerIdAndStartIsAfterOrderByStartDesc(owner.getId(), now, pageable);
                break;
            case WAITING:
                result = bookingRepository.findAllByItemOwnerIdAndStatusIsOrderByStartDesc(owner.getId(), StatusBooking.WAITING, pageable);
                break;
            case REJECTED:
                result = bookingRepository.findAllByItemOwnerIdAndStatusIsOrderByStartDesc(owner.getId(), StatusBooking.REJECTED, pageable);
                break;
        }
        return result.stream()
                .map(BookingMapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }

    public List<BookingResponseDto> stateToRepository(User owner, State state, Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> resultList = new ArrayList<>();
        Page<Booking> result = new PageImpl<>(resultList);
        switch (state) {
            case ALL:
                result = bookingRepository.findBookingsByBookerOrderByStartDesc(owner, pageable);
                break;
            case CURRENT:
                result = bookingRepository.findAllByBookerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(owner, now, now, pageable);
                break;
            case PAST:
                result = bookingRepository.findAllByBookerAndEndIsBeforeAndStatusIsOrderByStartDesc(owner, now, StatusBooking.APPROVED, pageable);
                break;
            case FUTURE:
                result = bookingRepository.findAllByBookerAndStartIsAfterOrderByStartDesc(owner, now, pageable);
                break;
            case WAITING:
                result = bookingRepository.findAllByBookerAndStatusIsOrderByStartDesc(owner, StatusBooking.WAITING, pageable);
                break;
            case REJECTED:
                result = bookingRepository.findAllByBookerAndStatusIsOrderByStartDesc(owner, StatusBooking.REJECTED, pageable);
                break;
        }
        return result.stream()
                .map(BookingMapper::toBookingResponseDto)
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

    public static Optional<State> checkState(String stateRequest) {
        for (State state : State.values()) {
            if (stateRequest.equals(state.toString())) {
                return Optional.of(State.valueOf(stateRequest));
            }
        }
        return Optional.empty();
    }

}