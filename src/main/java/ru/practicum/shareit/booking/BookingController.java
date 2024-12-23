package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private static final String OWNER = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoResponse create(@RequestHeader(OWNER) Long userId, @Valid @RequestBody BookingDtoRequest booking) {
        return bookingService.add(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse approved(@RequestHeader(OWNER) Long userId, @PathVariable Long bookingId,
                                       @RequestParam(defaultValue = "") Boolean approved) {
        return bookingService.approved(userId, bookingId, approved);
    }

    @GetMapping("/{id}")
    public BookingDtoResponse findById(@RequestHeader(OWNER) Long userId, @PathVariable Long id) {
        return bookingService.findById(userId, id);
    }

    @GetMapping("/owner")
    public List<BookingDtoResponse> getAllByOwner(@RequestHeader(OWNER) Long ownerId,
                                                  @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllByOwner(ownerId, state);
    }

    @GetMapping
    public List<BookingDtoResponse> getAllByBooker(@RequestHeader(OWNER) Long bookerId,
                                                   @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllByBooker(bookerId, state);
    }
}
