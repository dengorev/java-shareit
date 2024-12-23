package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.model.Comment;

@Component
public class CommentMapper {
    public CommentDtoResponse modelToDtoResponse(Comment comment) {
        return CommentDtoResponse.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }

    public Comment modelFromDto(CommentDtoRequest comment) {
        return Comment.builder()
                .text(comment.getText())
                .build();
    }
}
