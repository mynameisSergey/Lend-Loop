package ru.practicum.shareit.booking.model;

public enum BookingState {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    /**
     * Возвращает соответствующее состояние бронирования на основе строки.
     *
     * @param bookingState строковое представление состояния бронирования
     * @return состояние бронирования
     * @throws IllegalArgumentException если переданное состояние не соответствует ни одному из значений перечисления
     */
    public static BookingState from(String bookingState) {
        try {
            return BookingState.valueOf(bookingState);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown booking state: " + bookingState);
        }
    }
}