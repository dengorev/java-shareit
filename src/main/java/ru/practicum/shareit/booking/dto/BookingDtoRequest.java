package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDtoRequest {
    @NotNull
   private Long itemId;
    @NotNull
    @FutureOrPresent
   private LocalDateTime start;
    @NotNull
    @Future
   private LocalDateTime end;
}
