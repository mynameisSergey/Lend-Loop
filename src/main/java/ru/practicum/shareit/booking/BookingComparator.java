package ru.practicum.shareit.booking;

import java.util.Comparator;

public class BookingComparator implements Comparator<Booking> {

    @Override
    public int compare(Booking a, Booking b) {
        return a.getStart().compareTo(b.getStart());
    }
}