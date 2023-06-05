package ru.practicum.shareit.booking.model;

import java.util.Optional;

public enum State {
    WAITING, REJECTED, ALL, CURRENT, PAST, FUTURE;

    public static Optional<State> checkState(String stateRequest) {
        for (State state : State.values()) {
            if (stateRequest.equals(state.toString())) {
                return Optional.of(State.valueOf(stateRequest));
            }
        }
        return Optional.empty();
    }
}