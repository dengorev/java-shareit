package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemDtoWithComments {
    Long id;
    String name;
    String description;
    Boolean available;
    List<CommentDtoResponse> comments;
    LocalDateTime lastBooking;
    LocalDateTime nextBooking;
}

