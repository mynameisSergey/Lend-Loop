package ru.practicum.shareit.exception;

public class WrongDataException extends RuntimeException {
    public WrongDataException(final String message) {
        super(message);
    }
}