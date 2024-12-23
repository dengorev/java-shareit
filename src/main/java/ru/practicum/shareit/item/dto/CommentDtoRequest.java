package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CommentDtoRequest {
    @NotBlank
    @Length(max = 512)
    String text;
}
