package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя не должно быть пустым")
    @Size(max = 255, message = "Имя не должно превышать 255 символов")
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @NotBlank(message = "Описание не должно быть пустым")
    @Size(max = 255, message = "Описание не должно превышать 255 символов")
    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "available", nullable = false)
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @ToString.Exclude
    private User owner;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private ItemRequest itemRequest;
}