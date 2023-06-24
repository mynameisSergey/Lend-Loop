package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.Min;

/**
 * // TODO .
 */
@Data
@NoArgsConstructor(force = true)
@Entity
@Table(name = "items")
public class Item {
    @Min(0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private long id;
    @Column(name = "item_name")
    private String name;
    @Column(name = "item_description")
    private String description;
    @Column(name = "is_available")
    private Boolean available;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    @JoinColumn(name = "owner_id")
    private User owner;

    @JsonBackReference
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "request_id")
    private ItemRequest request;
    @Transient
    private long requestId;

    @PostLoad
    public void loadRequestId() {
        if (request != null)
            requestId = request.getId();
    }

    public Item(long id, String name, String description, boolean available, User owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }

    public Item(String name, String description, boolean available, User owner) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }

    public Item(String name, String description, boolean available, User owner, ItemRequest request) {
        System.out.println("::::::::::::::::::> " + request);
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = request;
        this.requestId = request.getId();
    }

    public Item(long id, String name, String description, Boolean available, User user, ItemRequest request) {
        System.out.println("::::::::::::::::::> " + request);
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = user;
        this.request = request;
        this.requestId = request.getId();
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setRequest(ItemRequest request) {
        this.request = request;
        this.requestId = request.getId();
    }
}