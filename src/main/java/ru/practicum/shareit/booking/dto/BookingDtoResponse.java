package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDtoForBooking;
import ru.practicum.shareit.user.dto.UserDtoBooking;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDtoResponse {
    private Long id;
    private ItemDtoForBooking item;
    private UserDtoBooking booker;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
}
