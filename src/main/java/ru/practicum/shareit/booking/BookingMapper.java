package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoForBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDtoBooking;
import ru.practicum.shareit.user.model.User;

@Component
public final class BookingMapper {
    public BookingDtoResponse modelToDto(Booking booking) {
        return BookingDtoResponse.builder()
                .id(booking.getId())
                .item(ItemDtoForBooking.builder()
                        .id(booking.getItem().getId())
                        .name(booking.getItem().getName())
                        .build())
                .booker(UserDtoBooking.builder()
                        .id(booking.getBooker().getId())
                        .build())
                .status(booking.getStatus())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();
    }

    public Booking modelFromDto(BookingDtoRequest booking, User user, Item item) {
        return Booking.builder()
                .booker(user)
                .item(item)
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();
    }
}
