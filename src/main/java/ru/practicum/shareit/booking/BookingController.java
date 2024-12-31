package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.List;

import static ru.practicum.shareit.booking.constants.Constants.OWNER;

@RestController
@Slf4j
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoResponse create(@RequestHeader(OWNER) Long userId, @Valid @RequestBody BookingDtoRequest booking) {
        log.info("Добавление вещи");
        return bookingService.add(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse approved(@RequestHeader(OWNER) Long userId, @PathVariable Long bookingId,
                                       @RequestParam(defaultValue = "false") Boolean approved) {
        log.info("Подтверждение вещи");
        return bookingService.approved(userId, bookingId, approved);
    }

    @GetMapping("/{id}")
    public BookingDtoResponse findById(@RequestHeader(OWNER) Long userId, @PathVariable Long id) {
        log.info("Поиск вещи с ID={}", id);
        return bookingService.findById(userId, id);
    }

    @GetMapping("/owner")
    public List<BookingDtoResponse> getAllByOwner(@RequestHeader(OWNER) Long ownerId,
                                                  @RequestParam(defaultValue = "ALL") String state) {
        log.info("олучение списка бронирований для всех вещей");
        return bookingService.getAllByOwner(ownerId, state);
    }

    @GetMapping
    public List<BookingDtoResponse> getAllByBooker(@RequestHeader(OWNER) Long bookerId,
                                                   @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получение списка всех бронирований");
        return bookingService.getAllByBooker(bookerId, state);
    }
}
