package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDtoResponse {
    Long id;
    String text;
    String authorName;
    LocalDateTime created;
}
