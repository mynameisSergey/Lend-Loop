package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date")
    @NotNull
    @FutureOrPresent
    private LocalDateTime start;
    @Column(name = "end_date")
    @NotNull
    @FutureOrPresent
    private LocalDateTime end;
    @OneToOne
    private Item item;
    @OneToOne
    private User booker;
    @Enumerated(EnumType.STRING)
    private StatusBooking status;

    public Booking(Booking oldBooking) {
        this.setId(oldBooking.getId());
        this.setBooker(oldBooking.getBooker());
        this.setStart(oldBooking.getStart());
        this.setEnd(oldBooking.getEnd());
        this.setStatus(oldBooking.getStatus());
        this.setItem(oldBooking.getItem());
    }
}