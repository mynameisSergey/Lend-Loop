package ru.practicum.shareit.comment;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CommentDto {
    private long id;
    private long itemId;
    private String authorName;
    private String text;
    private LocalDate created = LocalDate.now();

    public CommentDto(long id, String text, LocalDate created, long itemId, String name) {
        this.id = id;
        this.text = text;
        this.created = created;
        this.authorName = name;
        this.itemId = itemId;
    }

    public CommentDto(long id, String text, long itemId, String name) {
        this.id = id;
        this.text = text;
        this.authorName = name;
        this.itemId = itemId;
    }
}