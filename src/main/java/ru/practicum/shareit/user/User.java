package ru.practicum.shareit.user;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;

/**
 * // TODO .
 */
@Data
@Entity
@Table(name = "users")
public class User {
    @Min(0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;
    @Column(name = "user_name", nullable = false, length = 255)
    private String name;
    @Column(name = "user_email", nullable = false, length = 512)
    private String email;

    public User() {
    }

    public User(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}