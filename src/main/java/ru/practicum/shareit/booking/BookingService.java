package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.List;

public interface BookingService {
    BookingDtoResponse add(Long id, BookingDtoRequest booking);

    BookingDtoResponse approved(Long userId, Long bookingId, Boolean approved);

    BookingDtoResponse findById(Long userId, Long id);

    List<BookingDtoResponse> getAllByBooker(Long userId, String state);

    List<BookingDtoResponse> getAllByOwner(Long ownerId, String state);
}