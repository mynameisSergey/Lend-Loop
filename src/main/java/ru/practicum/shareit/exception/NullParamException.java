package ru.practicum.shareit.exception;

public class NullParamException extends RuntimeException {
    public NullParamException(final String message) {
        super(message);
    }
}
