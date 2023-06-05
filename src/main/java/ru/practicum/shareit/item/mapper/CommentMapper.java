package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.Optional;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static void toComment(Comment comment, CommentDto commentDto) {
        comment.setId(commentDto.getId());
        Optional.ofNullable(commentDto.getText()).ifPresent(comment::setText);
        Optional.ofNullable(commentDto.getCreated()).ifPresent(comment::setCreated);
    }
}