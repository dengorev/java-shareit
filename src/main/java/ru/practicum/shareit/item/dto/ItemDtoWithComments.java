package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemDtoWithComments {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private List<CommentDtoResponse> comments;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
}

